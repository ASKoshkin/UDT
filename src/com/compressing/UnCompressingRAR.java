/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compressing;

import com.task.ITask;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import org.itadaki.bzip2.BZip2InputStream;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class UnCompressingRAR implements IUnCompressing {
    private ITask task;
    private UDTLogger MyLogger;
    
    private UnCompressingRAR() {
    }
    
    public static UnCompressingRAR getInstance() {        
        return new UnCompressingRAR();
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
    public boolean Run() {
        boolean Res = false;
        
        MyLogger.LogSend("Start Decompressing");
        
        FileInputStream in;
        int bytesRead;
        byte[] decoded = new byte [524288];
        FileOutputStream out = null;
        BZip2InputStream bzIn = null;
        
        try {
            in = new FileInputStream(task.getPathConvert()+task.getFileDownload());
            out = new FileOutputStream(task.getPathConvert()+task.getFile());
            bzIn = new BZip2InputStream(in, false);
            
            while ((bytesRead = bzIn.read (decoded)) != -1) {
                        out.write (decoded, 0, bytesRead) ;        
            }
            
            MyLogger.LogSend("End Decompressing");
            Res = true;
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
        
        return Res;
    }
}
