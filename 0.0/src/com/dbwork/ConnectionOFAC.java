/*
 * Класс для работы с таблицей PASSPORT
 * 
 * 
 */
package com.dbwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import udt.IReport;
import udt.ISettings;
import udt.ReportHTML;
import udt.SettingsXML;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class ConnectionOFAC implements IConnection {
    private java.sql.Connection Conn;
    private Statement stmt;
    
    private final String url = "jdbc:sqlserver://";
    private String ServerName; 
    private final String PortNumber = "1433";
    private String DatabaseName; 
    private String UserName; 
    private String Password;
    
    private ISettings MySettings;
    private UDTLogger MyLogger; 
    private IReport MyReport; 
    /*-----------------------------------------------------------------------*/
    private ConnectionOFAC() {
    }
    
    public static ConnectionOFAC getInstance() {
        return ConnectionOFACHolder.INSTANCE;
    }
    
    private static class ConnectionOFACHolder {
        private static final ConnectionOFAC INSTANCE = new ConnectionOFAC();
    }
    /*-----------------------------------------------------------------------*/

    @Override
    public boolean Run() {
        boolean Res = false;
        boolean Execute;
        int step=1;
        
        if (!Init()){            
            return false;
        }  
        
        try {            
            MyLogger.LogSend("Connection Successful!");
            stmt = Conn.createStatement();
            
            do{
            MyLogger.LogSend("Start truncate");
            stmt.execute("truncate table "+MySettings.getTable());
              
            MyLogger.LogSend("Truncate execute");
                          
            MyLogger.LogSend("Start SHRINKFILE");
            stmt.execute("DBCC SHRINKFILE ("+MySettings.getDataBase()+"_log)");
               
            MyLogger.LogSend("End SHRINKFILE");
            step = step+1;
            
            waitetime1(1);
            
            Execute = BCPRun();
            
            }while(!Execute && step<5 );
            MyReport.setCreateIndex(CreateIndex());
            MyReport.setCountRecDB(getCountRecord());
            
            MyReport.setDateEnd(new Date());
            MyReport.setFileName(MySettings.getTableUpdate().toString());
            MyReport.SaveReport();
            
            Res = true;
       
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

    
    // инициализация параметров
    private boolean Init(){
        boolean Res = false;
        
        MySettings = SettingsXML.getInstance();
        MyLogger = UDTLogger.getInstance();
        MyReport = ReportHTML.getInstance();
        
        ServerName = MySettings.getServer();
        DatabaseName = MySettings.getDataBase();
        UserName = MySettings.getUser();
        Password = MySettings.getPW();       
        
        try{
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
            
            if (MySettings.isAutorizSQL()) {
                Conn = java.sql.DriverManager.getConnection(getConnectionUrl1(),UserName,Password);
                   
            } else {
                Conn = java.sql.DriverManager.getConnection(getConnectionUrl());
            }
               
            if (Conn!=null) {
                Res=true;
            }
            
        }catch(ClassNotFoundException | SQLException e){
            MyLogger.LogSend(Level.SEVERE,"Class Not Found: " + e.getMessage());                            
        
        }
        return Res;
    }
    
    // у таблицы OFAC нету индекса
    @Override
    public boolean DropIndex() {
        return false;
    }

    // у таблицы OFAC нету индекса
    @Override
    public boolean CreateIndex() {
        return false;
    }

    @Override
    public int getCountRecord() {
        int Res = 0;
        
        ResultSet rs = null;
        try {
            stmt = Conn.createStatement();
            rs = stmt.executeQuery("select count(*) as cnt from "+MySettings.getTable());
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
    
    private String getConnectionUrl(){
         return url+ServerName+":"+PortNumber+";databaseName="+DatabaseName+";integratedSecurity=true;";
    }
    
     private String getConnectionUrl1(){
         return url+ServerName+":"+PortNumber+";databaseName="+DatabaseName;
    }

    @Override
    public boolean BCPRun() {
        boolean Res = true;
        
        if (!CheckFile()){            
            MyLogger.LogSend("Файл "+MySettings.getPathConvert()+MySettings.getFileConverted()+" не найден.");
            return false;
        }
        
        // с какой строки начинаем импорт
        String F="-F1";
                
        Runtime rt = Runtime.getRuntime();
        String SExec;
        try {
            MyLogger.LogSend("Start bcp");
            
            if (MySettings.isAutorizSQL()){
                SExec="bcp.exe "+MySettings.getDataBase()+"."+MySettings.getTable()+" in "+MySettings.getPathConvert()+MySettings.getFileConverted()+" "+F+ " -b50000 -c -t , -CACP "+
                "-S"+MySettings.getServer()+" -U "+MySettings.getUser()+" -P"+MySettings.getPW();
            } else {
                SExec="bcp.exe "+MySettings.getDataBase()+"."+MySettings.getTable()+" in "+MySettings.getPathConvert()+MySettings.getFileConverted()+" "+F+" -b50000 -c -t , -CACP "+
                "-S"+MySettings.getServer()+" -T";
            }
                                  
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
        boolean Res;
        Res=false;
        
        File f;
        f=new File(MySettings.getPathConvert()+MySettings.getFileConverted());
        
        Res = f.exists();
        return Res;
    }
     
     
    
}
