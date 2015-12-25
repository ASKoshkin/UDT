/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.download;

import java.net.URL;
import udt.ASettings;
import udt.SettingsXML;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class DownLoadHTTPS extends ADownload {
    private static ASettings MySettings;
    private URL url;
    private UDTLogger MyLogger;    
    private boolean isContinueDownload;
    private final int timeout = 120000;
    private long ContentSize; // размер контента на сервере.

    private DownLoadHTTPS() {
    }
    
    public static DownLoadHTTPS getInstance() {
        MySettings = SettingsXML.getInstance();
        
        return DownLoadHTTPS.DownloadHTTPSHolder.INSTANCE;
    }
    
    private static class DownloadHTTPSHolder {
        
        private static final DownLoadHTTPS INSTANCE = new DownLoadHTTPS();
    }
    
    @Override
    public boolean run() {
       boolean Res = false;
       
       return Res;
    }
    
    
}
