/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compressing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import org.itadaki.bzip2.BZip2InputStream;
import udt.ASettings;
import udt.SettingsXML;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class UnCompressingRAR extends AUnCompressing {
    
    private UnCompressingRAR() {
    }
    
    public static UnCompressingRAR getInstance() {
        return UnCompressingRARHolder.INSTANCE;
    }

    private static class UnCompressingRARHolder {

        private static final UnCompressingRAR INSTANCE = new UnCompressingRAR();
    }
    
    @Override
    public boolean Run() {
        UDTLogger MyLogger = UDTLogger.getInstance();
        ASettings MySettings = SettingsXML.getInstance();
        
        MyLogger.LogSend("Start Decompressing");
        
        FileInputStream in;
        int bytesRead;
        byte[] decoded = new byte [524288];
        FileOutputStream out = null;
        BZip2InputStream bzIn = null;
        
        try {
            in = new FileInputStream(MySettings.getPathConvert()+MySettings.getFileArhive());
            out = new FileOutputStream(MySettings.getPathConvert()+MySettings.getFile());
            bzIn = new BZip2InputStream(in, false);
            
            while ((bytesRead = bzIn.read (decoded)) != -1) {
                        out.write (decoded, 0, bytesRead) ;        
                }
            
            MyLogger.LogSend("End Decompressing");
        } catch (FileNotFoundException ex) {            
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());
        } catch (IOException ex) {
            
            MyLogger.LogSend(Level.SEVERE,ex.getMessage());
        } finally{
            try {
                out.close();
                bzIn.close();
            } catch (IOException ex) {               
                MyLogger.LogSend(Level.SEVERE,ex.getMessage());
            }
            
        }
        
        
        return true;
    }
    
    
}
