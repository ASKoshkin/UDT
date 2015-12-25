/*
 * Абстрактый класс. 
 * оставлю возможность считывать файл нестроек не только с xml но и с других форматов
 * для этого метод ReadFile сделаю абстрактным.
 */


package udt;

/**
 *
 * @author koshkin_as
 */
public abstract class ASettings implements ISettings{
    protected String url;
    protected String FileArhive;
    protected String File;
    protected String PathConvert;
    protected String FileConverted;
    protected boolean Download;
    
    protected boolean AutorizSQL;

    protected String User;
    protected String PW;
    protected String Server;
    protected String DataBase;
    protected String Table;
    
    protected boolean Log;
    protected String Path_Log;
    
    protected String ProxyHost;
    protected String ProxyPort;
    
    protected boolean ReCreateIndex;
    
    // какую таблицу обновляем    
    protected TableEnum TableUpdate;
        
    
    @Override
    public abstract boolean ReadSettings();
    
    /**
     * 
     * @return Возвращает true если файл существует, иначе false
     */
    public abstract boolean CheckFile();
    
    
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getFileArhive() {
        return FileArhive;
    }

    @Override
    public String getFile() {
        return File;
    }

    @Override
    public String getFileConverted() {
        return FileConverted;
    }

    @Override
    public boolean isDownload() {
        return Download;
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
    public String getPathConvert() {
        return PathConvert;
    }

    @Override
    public String getTable() {
        return Table;
    }

    @Override
    public boolean isAutorizSQL() {
        return AutorizSQL;
    }

    @Override
    public boolean isLog() {
        return Log;
    }

    @Override
    public String getPath_Log() {
        return Path_Log;
    }

    @Override
    public String getProxyHost() {
        return ProxyHost;
    }

    @Override
    public String getProxyPort() {
        return ProxyPort;
    }

    @Override
    public boolean isReCreateIndex() {
        return ReCreateIndex;
    }
        
    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public TableEnum getTableUpdate() {
        return TableUpdate;
    }

    @Override
    public void setTableUpdate(TableEnum TableUpdate) {
        this.TableUpdate = TableUpdate;
    }    

}
