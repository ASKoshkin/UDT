/*
 * Класс для работы с таблицей PASSPORT
 * 
 * 
 */
package com.dbwork;

import com.task.ITask;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.Report.ReportData;
import udt.ISettings;
import udt.SettingsXML;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class ConnectionPASSPORT implements IConnection {
    
    private java.sql.Connection Conn;
    private Statement stmt;
    
    private final String url = "jdbc:sqlserver://";
    private final String PortNumber = "1433";
    
    private UDTLogger MyLogger;
    private ITask task;
    private ReportData rpData;
    
    private ISettings MySettings;
    
    private ConnectionPASSPORT() {
    }
    
    public static ConnectionPASSPORT getInstance() {
        return ConnectionPASSPORTHolder.INSTANCE;
    }

    @Override
    public void setTask(ITask task) {
        this.task = task;
    }

    @Override
    public void setLogger(UDTLogger logger) {
        this.MyLogger = logger;
    }
    
    @Override
    public void setReportData(ReportData rpData) {
        this.rpData = rpData;
    }

    private static class ConnectionPASSPORTHolder {

        private static final ConnectionPASSPORT INSTANCE = new ConnectionPASSPORT();
    }
    
    @Override
    public boolean Run() {
        boolean Res = false;
        int step = 1;
        if (!Init()){
            return false;
        }        
        try {            
            
            MyLogger.LogSend("Connection Successful!");
            stmt = Conn.createStatement();
            boolean Execute;
            do{            
                MyLogger.LogSend("Start truncate");
                stmt.execute("truncate table "+task.getTable());              
                MyLogger.LogSend("Truncate execute");
                          
                MyLogger.LogSend("Start SHRINKFILE");
                stmt.execute("DBCC SHRINKFILE ("+task.getDataBase()+"_log)");
               
                MyLogger.LogSend("End SHRINKFILE");
            
                DropIndex();
            
                waitetime1(1);
                step=step+1;
                Execute = BCPRun();
            }while (!Execute && step<5);
            
            rpData.setCreateIndex(CreateIndex());
            rpData.setCountRecDB(getCountRecord());
            rpData.setFileName(task.getName());
            
            Res = Execute;
       
        } catch (SQLException e) {             
             MyLogger.LogSend(Level.SEVERE,"Error Trace in getConnection() : "+e.getMessage());
        }
        
        return Res;
    }
    
    /**
     * Задержка в минутах
     * @param min сколько минут ждем
     */
    private void waitetime1(int min){
        byte n=0;
        int minEx = min*60;
        MyLogger.LogSend(Level.SEVERE,"Wait: "+min+" min");
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                MyLogger.LogSend(Level.SEVERE,ex.getMessage()); 
            }
            n++;
        } while (n<minEx);
    }
    
    private boolean Init(){
        boolean Res = false;              
        
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
               
            if (task.isAutorizSQL()) {
                Conn = java.sql.DriverManager.getConnection(getConnectionUrl1(),task.getUser(),task.getPW());
                   
            } else {
                Conn = java.sql.DriverManager.getConnection(getConnectionUrl());
            }
               
            if (Conn!=null) {
                Res=true;
            
            }
        }catch(ClassNotFoundException e){
            MyLogger.LogSend(Level.SEVERE,"Class Not Found: " + e.getMessage());                
        
        } catch (SQLException ex) {
            MyLogger.LogSend(Level.SEVERE,"Class Not Found: " + ex.getMessage());                
        }
        return Res;
    }

    @Override
    public boolean DropIndex() throws SQLException {
        boolean Res = false;
        if (task.isReCreateIndex()){
            stmt = Conn.createStatement();               
            stmt.execute("if exists(SELECT 1 FROM sysindexes WHERE Name='ind1') drop index ind1 on  "+task.getDataBase()+"."+task.getTable());            
            MyLogger.LogSend("The index (ind1) has been deleted"); 
            Res=true;
        }
        return Res;
    }

    @Override
    public boolean CreateIndex() {
        boolean Res = false;
        
        // если это таблица Ofac то индекс не пересоздаем, данных мало.
        
        if (task.isReCreateIndex()){
            try {
                stmt = Conn.createStatement();
                stmt.execute("if not exists(SELECT 1 FROM sysindexes WHERE Name='ind1') create index ind1 on  "+task.getDataBase()+"."+task.getTable()+" (seria,number)");                
                MyLogger.LogSend("The index (ind1) has been created"); 
                Res=true;
            } catch (SQLException ex) {
               
                MyLogger.LogSend(Level.SEVERE,ex.getMessage());
            }
        }
        return Res;
    }

    @Override
    public int getCountRecord() {
        int Res = 0;
        
        ResultSet rs = null;
        try {
            stmt = Conn.createStatement();
            rs = stmt.executeQuery("select count(*) as cnt from "+task.getTable());
            rs.next();
            
            Res=rs.getInt(1);
            
        } catch (SQLException ex) {
            
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());
        } finally{
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                
                MyLogger.LogSend(Level.SEVERE,ex.getMessage());
            }
        }
        
        return Res;
    }

    @Override
    public void CloseConnectionDB() {
        try {
            if (Conn !=null)
                Conn.close();
            Conn = null;
        } catch (SQLException ex) {
            
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());
        }
    }

    @Override
    public boolean BCPRun() {
        
        boolean Res = true;
        
        MySettings = SettingsXML.getInstance();
        
        if (!CheckFile()){            
            MyLogger.LogSend("Файл "+task.getPathConvert()+task.getFileImport()+" не найден.");
            return false;
        }
        
        // с какой строки начинаем импорт
        String F="-F2";
                
        Runtime rt = Runtime.getRuntime();
        String SExec;
        try {
            MyLogger.LogSend("Start bcp");
            
            if (task.isAutorizSQL()){
                SExec=MySettings.getBCP()+" "+task.getDataBase()+"."+task.getTable()+" in "+task.getPathConvert()+task.getFileImport()+" "+F+ " -b50000 -c -t , -CACP "+
                "-S"+task.getServer()+" -U "+task.getUser()+" -P"+task.getPW();
            } else {
                SExec=MySettings.getBCP()+" "+task.getDataBase()+"."+task.getTable()+" in "+task.getPathConvert()+task.getFileImport()+" "+F+" -b50000 -c -t , -CACP "+
                "-S"+task.getServer()+" -T";
            }
            
            System.out.println(SExec);
            
            Process pr = rt.exec(SExec);
            
            InputStreamReader in =  new InputStreamReader(pr.getInputStream());
            BufferedReader subProcessInputReader = new BufferedReader(in);
            
            String line;
            Pattern pattern = Pattern.compile(".*Error.*");
            Matcher m;
            while ((line = subProcessInputReader.readLine()) != null){
                MyLogger.LogSend(line);    

                /* проверка что bcp не вернул Error */
                m = pattern.matcher(line);
                if (m.matches()){
                   Res = false; 
                }
            }
                
            pr.waitFor();
            
            MyLogger.LogSend("End bcp");    
        } catch (IOException ex) {
            Logger.getLogger(ConnectionOFAC.class.getName()).log(Level.SEVERE, null, ex);
            Res = false;
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionOFAC.class.getName()).log(Level.SEVERE, null, ex);
            Res = false;
        }
        
        return Res;
    }
    
    // проверка существования файла
    private boolean CheckFile(){
        boolean Res = false;
        
        File f;
        f=new File(task.getPathConvert()+task.getFileImport());
        
        Res = f.exists();
        return Res;
    }    
    
     private String getConnectionUrl(){
         return url+task.getServer()+":"+PortNumber+";databaseName="+task.getDataBase()+";integratedSecurity=true;";
    }
    
     private String getConnectionUrl1(){
         return url+task.getServer()+":"+PortNumber+";databaseName="+task.getDataBase();
    }
    
     
    
}
