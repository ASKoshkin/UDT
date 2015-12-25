/*
 * Конвертация файла с недействительными паспортами
 * 
 * 
 */
package com.convert;

import com.Report.ReportData;
import com.task.ITask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class ConvertPASSPORT implements IConvert {
    private ITask task;
    private UDTLogger MyLogger;
    private ReportData rpData;
    
    private ConvertPASSPORT() {
    }
    
    public static ConvertPASSPORT getInstance() {
        return ConvertPASSPORTHolder.INSTANCE;
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
    
    private static class ConvertPASSPORTHolder {

        private static final ConvertPASSPORT INSTANCE = new ConvertPASSPORT();
    }
    
    @Override
    public boolean Run() {
        
        MyLogger.LogSend("Start Convert");
        BufferedReader in = null;
        BufferedWriter bufferedWriter = null;
        FileOutputStream fileOutputStream;
        try {
            
            File file = new File(task.getPathConvert()+task.getFile()); // распакованный файл
            
            File file_1 = new File(task.getPathConvert()+task.getFileImport()); // сконвертированный файл
            
            file.createNewFile();
            
            fileOutputStream = new FileOutputStream(file_1);
            
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "Cp1251"));
            
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8") );
            
            String s;
            int NumStr = 0;
            
            while ((s = in.readLine()) != null) {
                bufferedWriter.append(s+"\r\n");
                bufferedWriter.flush();
                NumStr++;
            }            
            
            MyLogger.LogSend("End Convert");            
            rpData.setCountStrRead(NumStr);
        } catch (FileNotFoundException ex) {           
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());
            return false;
        } catch (IOException ex) {            
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());
            return false;
        } finally {
            try {
                in.close();
                bufferedWriter.close();
            } catch (IOException ex) {               
                MyLogger.LogSend(Level.SEVERE,ex.getMessage());
                return false;
            }
        }
        return true;
    }
}
