/**
 * 
 */
package udt;

/**
 *
 * @author koshkin_as
 */
public interface ISettings {
        
    /**
     * Чтение файла настроек.
     * Данный метод абстрактный, возможно реализовать чтение из других форматов
     * 
     * @return При успешном чтение файла возвращает true иначе false
     */
    public boolean ReadSettings();
    
    public String getUrl();

    public String getFileArhive();

    public String getFile();
    
    public String getFileConverted();

    public boolean isDownload();

    public String getUser();

    public String getPW();

    public String getServer();

    public String getDataBase();

    public String getPathConvert();

    /**
     * Возвращает название таблицы в БД.
     * @return 
     * Возвращает название типа String
     */
    public String getTable();

    public boolean isAutorizSQL();

    public boolean isLog();

    public String getPath_Log();

    public String getProxyHost();

    public String getProxyPort();

    public boolean isReCreateIndex();
            
    public void setUrl(String url);

    /**
     * Возвращает название обновлямых данных.
     * @return 
     * Возвращает данные типа TableEnum.
     */
    public TableEnum getTableUpdate();

    /**
     * Передает экземпляру класса какую таблицу будем обновлять.
     * Тип передавамего значения: TableEnum
     * 
     * @param TableUpdate должен быть типа TableEnum, т.е. какая таблица обновляется
     */
    public void setTableUpdate(TableEnum TableUpdate);
    
    // Какую таблицу обновляем 
    public enum TableEnum{
        Passport 
        ,Ofac    
    }
    
}