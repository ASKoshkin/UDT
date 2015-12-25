/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.download;

import com.task.ITask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import udt.UDTLogger;

/**
 *
 * @author Alexey
 */
public class FactoryDownload {
    
    
    private FactoryDownload() {
    }
    
    public static synchronized FactoryDownload getInstance() {
        return FactoryDownloadHolder.INSTANCE;
    }
    
    private static class FactoryDownloadHolder {

        private static final FactoryDownload INSTANCE = new FactoryDownload();
    }

    
    public synchronized IDownload getDownload(ITask task, UDTLogger logger){
        IDownload dwnld = null;
        
        Pattern pattern = Pattern.compile("^HTTP://.*");
        Matcher matcher = pattern.matcher(task.getUrl().toUpperCase());
        if (matcher.matches()){
            dwnld = DownloadHTTP.getInstance();
            dwnld.setTask(task);
            dwnld.setLogger(logger);
        }
        
        pattern = Pattern.compile("^HTTPS://.*");
        matcher = pattern.matcher(task.getUrl().toUpperCase());
        if (matcher.matches()){
            dwnld = DownloadHTTPS.getInstance();
            dwnld.setTask(task);
            dwnld.setLogger(logger);
        }
        
        return dwnld;
    }
    
    
}
