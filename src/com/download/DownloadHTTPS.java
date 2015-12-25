/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.download;


import com.task.ITask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import javax.net.ssl.HttpsURLConnection;
import udt.ASettings;
import udt.SettingsXML;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class DownloadHTTPS implements IDownload {
    private static ASettings MySettings;
    private URL url;
    private UDTLogger MyLogger;    
    private boolean isContinueDownload;
    private final int timeout = 240000;
    private long ContentSize; // размер контента на сервере.

    private ITask task;
            
    private DownloadHTTPS() {
    }
    
    public static DownloadHTTPS getInstance() {
        MySettings = SettingsXML.getInstance();
        
        return new DownloadHTTPS();
    }

    @Override
    public void setTask(ITask task) {
        this.task = task;
    }
    
    @Override
    public void setLogger(UDTLogger logger) {
        this.MyLogger = logger;
    }
    
    
    /*
    private static class DownloadHTTPSHolder {
        
        private static final DownloadHTTPS INSTANCE = new DownloadHTTPS();
    }
    */
    @Override
    public boolean run() {
       boolean Res = false;
       
       //MyLogger = UDTLogger.getInstance();       
       SetEnviroment();
       
       DeleteFile();
        if (!Init()){
            // не смог установить соединение с сервером, выйду. 
            return false;            
        } 
        
        isContinueDownload=isContinueDownload();
        
        ContentSize = getSizeContent();
        
       if (isContinueDownload){
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
            System.setProperty("https.proxyHost", MySettings.getProxyHost());
            System.setProperty("https.proxyPort", MySettings.getProxyPort());
        }
    }
    
    private boolean Init(){
        boolean Res = false; 
        HttpsURLConnection conn; 
        try {
            url = new URL(task.getUrl()+task.getFileDownload());
            conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setRequestMethod("GET");
            
            Res = (conn.getResponseCode() == HttpsURLConnection.HTTP_OK);
            
        } catch (MalformedURLException ex) {
            MyLogger.LogSend(Level.SEVERE,"Init(), "+ex.getMessage()); 
        } catch (IOException ex) {
            MyLogger.LogSend(Level.SEVERE,"Init(), "+ex.getMessage()); 
        }
                
        return Res;
    }
    
     // проверка на возможность докачивать файл.
    private boolean isContinueDownload() {
        HttpsURLConnection connEx;
        boolean Res = false;
        
        try {
            connEx = (HttpsURLConnection) url.openConnection();
            connEx.setConnectTimeout(timeout);
            connEx.setReadTimeout(timeout);
            if (connEx.getResponseCode()==HttpsURLConnection.HTTP_OK){
                Res = connEx.getHeaderField("Accept-Ranges").equals("bytes");
                connEx.disconnect();
            }
        } catch (IOException ex) {
            MyLogger.LogSend(Level.SEVERE,"isContinueDownload(), "+ex.getMessage());
        }
        
        return Res;
    }
    
     /**
     * Возвращает размер контента на сервере
     * @return 
     */
    private int getSizeContent(){
        int Res = 0;
        HttpsURLConnection scn;
        try {            
            scn = (HttpsURLConnection) url.openConnection();            
            scn.setConnectTimeout(timeout);
            scn.setReadTimeout(timeout);                       
            Res = scn.getContentLength();            
            
            scn.disconnect();
            
            MyLogger.LogSend(Level.SEVERE,"Size on server: "+Res); 
            
        } catch (MalformedURLException ex) {
            MyLogger.LogSend(Level.SEVERE,"getSizeContent(), "+ex.getMessage()); 
        } catch (IOException ex) {
            MyLogger.LogSend(Level.SEVERE,"getSizeContent(), "+ex.getMessage()); 
        }
        
        return Res;
    }
    
    private boolean DeleteFile(){
        boolean Res = true;
        File fs = new File(task.getPathConvert()+task.getFileDownload());
        
        if (fs.exists()){
            Res =  fs.delete();
        }         
        return Res;
    }
    
    // Если сервер не позволяет докачивать файлы, то придеться всегда начинать с начало
    
    private boolean Download1(){
        boolean Res = false;        
        HttpsURLConnection conn;        
        
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
        HttpsURLConnection conn;
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
     * Возвращает ссылку на соединение в зависмости от возможности докачки файла.
     * Если докачка возможна, то проверяет размер файла на диске и запрашивает данные 
     * в соответствие с уже полученными данными.
     * @return 
     * Возвращает тип HttpURLConnection
     */
    private HttpsURLConnection getConnection(){
        
        HttpsURLConnection huc;
        Long FileSize; // размер на диске
        try {
            huc = (HttpsURLConnection) url.openConnection();
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
        
        return huc;
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
    private boolean download(HttpsURLConnection huc){
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
            MyLogger.LogSend(Level.SEVERE,"download(), "+ex.getMessage()); 
        } catch (IllegalArgumentException ex){
            MyLogger.LogSend(Level.SEVERE,"download(), "+ex.getMessage());         
            
        } finally{
            try {
                is.close();
            } catch (IOException ex) {
                MyLogger.LogSend(Level.SEVERE,"download(), "+ex.getMessage()); 
            }
        }
        return Res;
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
                    MyLogger.LogSend(Level.SEVERE,"getOutputStream(), "+ex.getMessage());
                }
            }
        }
        
        if (fos == null){
            try {            
                // открою файл для записи
                fos = new FileOutputStream(task.getPathConvert()+task.getFileDownload(),false);
            } catch (FileNotFoundException ex) {
                MyLogger.LogSend(Level.SEVERE,"getOutputStream(), "+ex.getMessage());
            }
        }
        
        return fos;
        
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
        
        //File fs = new File(task.getPathConvert()+task.getFileDownload());
        long fs = getFileSize();
        
        if (fs==ContentSize){
            Res = true;
        } else if (fs>ContentSize){
            //IllegalArgumentException
            throw new IllegalArgumentException("File size on disk larger than a file on a server");
        }
            
        return Res;
    }

    
}
