/*
 *  Конвертация файла OFAC
 * 
 * 
 */
package com.convert;

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
import udt.IReport;
import udt.ISettings;
import udt.ReportHTML;
import udt.SettingsXML;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class ConvertOFAC implements IConvert {
    
    private ConvertOFAC() {
    }
    
    public static ConvertOFAC getInstance() {
        return ConvertOFACHolder.INSTANCE;
    }
        
    private static class ConvertOFACHolder {

        private static final ConvertOFAC INSTANCE = new ConvertOFAC();
    }
    
    @Override
    public boolean Run() {
        UDTLogger MyLogger = UDTLogger.getInstance();
        ISettings MySettings = SettingsXML.getInstance();
        IReport MyReport = ReportHTML.getInstance();
                
        MyLogger.LogSend("Start Convert");
        BufferedReader in = null;
        BufferedWriter bufferedWriter = null;
        FileOutputStream fileOutputStream;
        try {
            
            File file = new File(MySettings.getPathConvert()+MySettings.getFile()); // распакованный файл
            
            File file_1 = new File(MySettings.getPathConvert()+MySettings.getFileConverted()); // сконвертированный файл
            
            file.createNewFile();
            
            fileOutputStream = new FileOutputStream(file_1);
            
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "Cp1251"));
            
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "CP1251") ); //"UTF8"
            
            String s;
            int NumStr = 0;
            
            while ((s = in.readLine()) != null) {
                
                String replaceAll = getNormalString(s);
                
                if (replaceAll.length()>11){                    
                    bufferedWriter.append(replaceAll+"\r\n");
                    bufferedWriter.flush();
                }
                NumStr++;
            }            
            
            MyLogger.LogSend("End Convert");            
            MyReport.setCountStrRead(NumStr);
            
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
    
    private String getNormalString(String s){        
        boolean SubStr = false;
        char kv = '"';
        char zp = ',';
        StringBuilder sb = new StringBuilder();
        
        // Пробегаю по строке. Если в первый раз встрачаю знак " считаю, что
        // запятые внутри нужно удалять, так как это строка внутри строки.        
        for (int num=0; num<s.length(); num++){                        
            if (s.charAt(num) == kv){
                SubStr = !SubStr;
            } else if (!(SubStr && s.charAt(num)==zp)){                                        
                sb.append(s.charAt(num));
            }
                            
        }                
        return sb.toString();        
    }
    
}
