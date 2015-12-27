/*
 * 
 * Класс скачивания файла по HTTP
 * 
 */
package com.download;

import com.task.ITask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import udt.ASettings;
import udt.SettingsXML;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */

public class DownloadHTTP implements IDownload{
    private static ASettings MySettings;
    private URL url;
    private UDTLogger MyLogger;    
    private boolean isContinueDownload;
    private final int timeout = 120000;
    private long ContentSize; // размер контента на сервере.
    private ITask task;
    
    private DownloadHTTP() {
    }
    
    
    public static DownloadHTTP getInstance() {
        MySettings = SettingsXML.getInstance();
        
        //return DownloadHTTPHolder.INSTANCE;
        return new DownloadHTTP();
    }

    /*
    private static class DownloadHTTPHolder {
    private static final DownloadHTTP INSTANCE = new DownloadHTTP();
    }
     */
    @Override
    public void setTask(ITask task) {
        this.task = task;
    }
    
    @Override
    public void setLogger(UDTLogger logger) {
        this.MyLogger = logger;
    }
    
    @Override
    public boolean run() {
        boolean Res = false;
        System.out.println("Скачивание файла по задаче "+task.getName());
        
       
        SetEnviroment();
        DeleteFile();
        if (!Init()){
            // не смог установить соединение с сервером, выйду. 
            // return false;
        }                
        isContinueDownload=isContinueDownload();
        
        ContentSize = getSizeContent();
        
        if (isContinueDownload()){
            // если сервер разрешает докачку
            MyLogger.LogSend(Level.SEVERE,"Server support resuming"); 
            Res = Download2();
        } else {
            // если сервер не разрешает докачку            
            MyLogger.LogSend(Level.SEVERE,"Server does not support resuming"); 
            Res = Download1();
        }
        
        return Res;
    }
    
    
    /**
     * Для HTTP протокола устанваливаем свойства
     */
    private void SetEnviroment(){
        if (!MySettings.getProxyHost().isEmpty()){
            System.setProperty("http.proxyHost", MySettings.getProxyHost());
            System.setProperty("http.proxyPort", MySettings.getProxyPort());
        }
    }
    
    /**
     * Инициализирую переменные
     * @return 
     */
    private boolean Init(){
        boolean Res = false; 
        HttpURLConnection conn; 
        try {
            url = new URL(task.getUrl()+task.getFileDownload());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setRequestMethod("GET");
            
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                Res = true;
            }
            
        } catch (MalformedURLException ex) {
            MyLogger.LogSend(Level.SEVERE,ex.getMessage()); 
        } catch (IOException ex) {
            MyLogger.LogSend(Level.SEVERE,ex.getMessage()); 
        }
        
        return Res;
    }
    
    // проверка на возможность докачивать файл.
    private boolean isContinueDownload() {
        HttpURLConnection connEx;
        boolean Res = false;
        
        try {
            connEx = (HttpURLConnection) url.openConnection();
            connEx.setConnectTimeout(timeout);
            connEx.setReadTimeout(timeout);
            if (connEx.getResponseCode()==HttpURLConnection.HTTP_OK){
                Res = connEx.getHeaderField("Accept-Ranges").equals("bytes");
                connEx.disconnect();
            }
        } catch (IOException ex) {
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());
        }  catch (NullPointerException ex){
            MyLogger.LogSend(Level.SEVERE,"asdsa"+ex.getMessage());
        } 
        return Res;
    }
    
    // Если сервер не позволяет докачивать файлы, то придеться всегда начинать с начало
    
    private boolean Download1(){
        boolean Res = false;        
        HttpURLConnection conn;        
        
        int num = 1; //количество попыток скачать
                        
        do{                                                          
            MyLogger.LogSend(Level.SEVERE,"Download file step: "+num); 
 
            
            conn = getConnection();
            Res = download(conn);                
            
            
            if (!Res){
                waitetime1(1);
            }
            
            num++;
        } while (!Res && (num<=20));
        
        return Res;        
    }
    
    private boolean Download2(){
        HttpURLConnection conn;
        boolean Res = false;
        int  num = 1; //количество попыток скачать

        do{                                                          
            MyLogger.LogSend(Level.SEVERE,"Download file step: "+num); 
             
            if (ContentSize>0){
                conn = getConnection();
                Res = download(conn);                
            }             
            
            
            if (!Res){
                waitetime1(1);
            }
            
            num++;
                              
        } while (!Res && (num<=20));
                                
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
    
    /**
     * Проверка размера файла на диске с размером файла на сервере
     * Если размер на диске превысил размер на файла на сервере, то
     * вызывается исключение IllegalArgumentException
     * @param size - размер файла на сервере
     * @return 
     * возвращает true если размер файла совпадает
     */
    private boolean CheckFileSize() throws IllegalArgumentException {
        boolean Res  = false;
        
        long fs = getFileSize();
        
        if (fs==ContentSize){
                Res = true;
        } else if (fs>ContentSize){
            //IllegalArgumentException
            throw new IllegalArgumentException("File size on disk larger than a file on a server");
        }
            
        return Res;
    }
    
    /**
     * Размер файла на диске
     * @return 
     * возвращает размер 
     */
    private long getFileSize(){
        long Res =0;
        
        File fs = new File(task.getPathConvert()+task.getFileDownload());
        
        if (fs.exists()){
            Res = fs.length();
        }
        
        MyLogger.LogSend(Level.SEVERE,"File size on a disk: "+Res);
        return Res;
    }
    
    /**
     * Загрузка файла с сервера. 
     * После того как отработает ф-ия, необходимо закрыть fos
     * @param huc 
     * @param fos 
     */
    private boolean download(HttpURLConnection huc){
        boolean Res = false;
        FileOutputStream fos;
        InputStream is = null;
        byte[] buffer = new byte[1024];
        int ReadByte;
                
        fos = getOutputStream();        
        
        try {
            is = huc.getInputStream();
                                    
            while ((ReadByte = is.read(buffer))!=-1) {                                
                fos.write(buffer,0,ReadByte);
            }             
            
            fos.flush();
            fos.close();
            fos = null;
            
            /*Если размер файла не определен, считаю что файл скачался правильно*/
            if (ContentSize!=0){
                Res = CheckFileSize();
            } else{
                Res = true;
            }
            
        } catch (IOException ex) {
            MyLogger.LogSend(Level.SEVERE,ex.getMessage()); 
        } catch (IllegalArgumentException ex){
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());  
        } catch (Exception ex){
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());  
        } finally{
            try {
                if (is!= null){
                    is.close();
                }
            } catch (IOException ex) {
                MyLogger.LogSend(Level.SEVERE,ex.getMessage()); 
            }
        }
        return Res;
    }

    /**
     * Возвращает размер контента на сервере
     * @return 
     */
    private int getSizeContent(){
        int Res = 0;
        HttpURLConnection scn;
        try {            
            scn = (HttpURLConnection) url.openConnection();            
            scn.setConnectTimeout(timeout);
            scn.setReadTimeout(timeout);                       
            Res = scn.getContentLength();            
            
            scn.disconnect();
            
            MyLogger.LogSend(Level.SEVERE,"Size on server: "+Res); 
            
        } catch (MalformedURLException ex) {
            MyLogger.LogSend(Level.SEVERE,ex.getMessage()); 
        } catch (IOException ex) {
            MyLogger.LogSend(Level.SEVERE,ex.getMessage()); 
        }
        
        return Res;
    }
        
    
    /**
     * Возвращает ссылку на соединение в зависмости от возможности докачки файла.
     * Если докачка возможна, то проверяет размер файла на диске и запрашивает данные 
     * в соответствие с уже полученными данными.
     * @return 
     * Возвращает тип HttpURLConnection
     */
    private HttpURLConnection getConnection(){
        
        HttpURLConnection huc;
        Long FileSize; // размер на диске
        try {
            huc = (HttpURLConnection) url.openConnection();
            huc.setConnectTimeout(timeout);
            huc.setReadTimeout(timeout);
        } catch (IOException ex) {
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());
            return null;
        }
        
        if (isContinueDownload){            
            FileSize = getFileSize();
            if (FileSize>0){
                huc.setRequestProperty("Range","bytes="+FileSize+"-"); 
            }
            huc.setDoOutput(true);
        }
        
        MyLogger.LogSend(Level.SEVERE,"Get new connection"); 

        return huc;
    }
    
    private FileOutputStream getOutputStream(){
        FileOutputStream fos = null;
        Long FileSize;
        
        if (isContinueDownload){
            FileSize = getFileSize();
            if (FileSize>0){
                try {
                    // открою файл для записи.
                    fos = new FileOutputStream(task.getPathConvert()+task.getFileDownload(),true);
                } catch (FileNotFoundException ex) {
                    MyLogger.LogSend(Level.SEVERE,ex.getMessage());
                }
            }
        }
        
        if (fos == null){
            try {            
                // открою файл для записи
                fos = new FileOutputStream(task.getPathConvert()+task.getFileDownload(),false);
            } catch (FileNotFoundException ex) {
                MyLogger.LogSend(Level.SEVERE,ex.getMessage());
            }
        }
        
        return fos;
        
    }
    
    private boolean DeleteFile(){
        boolean Res = true;
        File fs = new File(task.getPathConvert()+task.getFileDownload());
        
        if (fs.exists()){
            Res =  fs.delete();
        }         
        return Res;
    }

    
    
}
