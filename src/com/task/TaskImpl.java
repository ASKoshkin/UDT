package com.task;

import java.util.Map;

/**
 *
 * @author Alexey
 */
public class TaskImpl implements ITask {
    
    private String  Name;
    private String  url;
    private String  FileDownload;
    private String  File;
    private String  PathConvert;
    private String  FileImport;
    private boolean Download;
    private boolean ReCreateIndex;
    private boolean AutorizSQL;
    private String  User;
    private String  PW;
    private String  Server;
    private String  DataBase;
    private String  Table;    
    private Map<String,String> SettingsMap;
    
    public static String[] PARAMETR_ARR ={"Name","url","File_download","File","Path_Converted",
    "File_import","Download","ReCreateIndex","AutorizSQL","User","PW","Server","DataBase",
    "Table"};
    
    public TaskImpl() {    
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFileDownload(String FileDownload) {
        this.FileDownload = FileDownload;
    }

    public void setFile(String File) {
        this.File = File;
    }

    public void setPathConvert(String PathConvert) {
        this.PathConvert = PathConvert;
    }

    public void setFileImport(String FileImport) {
        this.FileImport = FileImport;
    }

    public void setDownload(boolean Download) {
        this.Download = Download;
    }

    public void setReCreateIndex(boolean ReCreateIndex) {
        this.ReCreateIndex = ReCreateIndex;
    }

    public void setAutorizSQL(boolean AutorizSQL) {
        this.AutorizSQL = AutorizSQL;
    }

    public void setUser(String User) {
        this.User = User;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public void setServer(String Server) {
        this.Server = Server;
    }

    public void setDataBase(String DataBase) {
        this.DataBase = DataBase;
    }

    public void setTable(String Table) {
        this.Table = Table;
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getFileDownload() {
        return FileDownload;
    }

    @Override
    public String getFile() {
        return File;
    }

    @Override
    public String getPathConvert() {
        return PathConvert;
    }

    @Override
    public String getFileImport() {
        return FileImport;
    }

    @Override
    public boolean isDownload() {
        return Download;
    }

    @Override
    public boolean isReCreateIndex() {
        return ReCreateIndex;
    }

    @Override
    public boolean isAutorizSQL() {
        return AutorizSQL;
    }

    @Override
    public String getUser() {
        return User;
    }

    @Override
    public String getPW() {
        return PW;
    }

    @Override
    public String getServer() {
        return Server;
    }

    @Override
    public String getDataBase() {
        return DataBase;
    }

    @Override
    public String getTable() {
        return Table;
    }

    
    
    
    
    /**
     * Установк свйоств через Map
     * @param SettingsMap свойства задачи
     */
    @Override
    public void setSettingsMap(Map<String, String> SettingsMap) {
        this.SettingsMap = SettingsMap;
        
        Name = SettingsMap.get("Name");
        url= SettingsMap.get("url");
        FileDownload = SettingsMap.get("File_download");
        File = SettingsMap.get("File");
        PathConvert = SettingsMap.get("Path_Converted");
        FileImport = SettingsMap.get("File_import");
        Download = Boolean.parseBoolean(SettingsMap.get("Download"));
        ReCreateIndex= Boolean.parseBoolean(SettingsMap.get("ReCreateIndex"));
        AutorizSQL= Boolean.parseBoolean(SettingsMap.get("AutorizSQL"));
        User = SettingsMap.get("User");
        PW= SettingsMap.get("PW");
        Server= SettingsMap.get("Server");
        DataBase= SettingsMap.get("DataBase");
        Table= SettingsMap.get("Table");        
        
    }
    
    
    
    
}
