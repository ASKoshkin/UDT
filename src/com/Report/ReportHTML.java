/**
 * Сохранение отчета в html файл.
 * 
 */
package com.Report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author koshkin_as
 */
public class ReportHTML implements IReport{
    
    private Date DateStart;
    private Date DateEnd;
    
    private ReportHTML() {
    }
    
    public synchronized static ReportHTML getInstance() {
        return ReportHTMLHolder.INSTANCE;
    }
    
    private static class ReportHTMLHolder {

        private static final ReportHTML INSTANCE = new ReportHTML();
    }

    @Override
    public void setDateStart(Date DateStart) {
        if (this.DateStart == null){
            this.DateStart = DateStart;
        }
    }

    @Override
    public synchronized void SaveReport(ReportData data) {
        File file = new File("Report_"+data.getFileName()+".html");
        DateEnd = new Date();
        
        SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy hh:mm:ss");
        
        if (file.exists()) {
            if (!file.delete()){
                System.out.println("Не удалось удалить файл Report"+data.getFileName()+".html");
                return; // выйду, удалить не удалось.
            }
        }
        
        BufferedWriter bufferedWriter = null;
        FileOutputStream fileOutputStream = null;
        
        try {
            
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
       
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(fileOutputStream, "Cp1251"));
            
            bufferedWriter.append("<HTML>\n");
            bufferedWriter.append("<HEAD>\n");
            bufferedWriter.append("<TITLE> Отчет импорта </TITLE>\n");
            bufferedWriter.append("</HEAD>\n");
            bufferedWriter.append("<BODY>\n");
            
            bufferedWriter.append("<h1>\n");
            bufferedWriter.append("Отчет на "+ft.format(DateEnd)+" \n");
            bufferedWriter.append("</h1>\n");
            
            bufferedWriter.append("<p>\n");
            bufferedWriter.append("Время начала операции загрузки: "+ft.format(DateStart)+"\n");
            bufferedWriter.append("</p>\n");
            bufferedWriter.append("<p>\n");
            bufferedWriter.append("Время окончания операции загрузки: "+ft.format(DateEnd)+"\n");
            bufferedWriter.append("</p>\n");
            bufferedWriter.append("<p>\n");
            bufferedWriter.append("Количество записей в файле: "+data.getCountStrRead()+"\n");
            bufferedWriter.append("</p>\n");
            bufferedWriter.append("<p>\n");
            bufferedWriter.append("Количество записей в БД: "+data.getCountRecDB()+"\n");
            bufferedWriter.append("</p>\n");
            bufferedWriter.append("<p>\n");
            
            bufferedWriter.append("Индекс создан: "+data.isCreateIndex()+"\n");
            bufferedWriter.append("</p>\n");
            
            bufferedWriter.append("</BODY>\n");
            bufferedWriter.append("</HTML>\n");
            
        } catch (IOException ex) {
            Logger.getLogger(ReportHTML.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                bufferedWriter.close();
                fileOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ReportHTML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
