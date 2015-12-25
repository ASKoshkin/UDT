/*
 * Задачи 
 * 
 */
package com.task;

import java.util.Map;

/**
 *
 * @author Alexey
 */
public interface ITask {
    public String getName();
    public String getUrl();
    public String getFileDownload();
    public String getPathConvert();
    public void setSettingsMap(Map<String, String> SettingsMap);
    public String getFile();
    public String getTable();
    public String getFileImport();
    public String getDataBase();
    public boolean isAutorizSQL();
    public String getServer();
    public String getUser();
    public String getPW();
    public boolean isReCreateIndex();
    public boolean isDownload();
    
}
