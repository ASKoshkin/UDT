/**
 * Интерфейс класса отчета
 * 
 */
package udt;

import java.util.Date;

/**
 *
 * @author koshkin_as
 */
public interface IReport {

    /**
     * Передача даты начала обновления
     * @param DateStart тип Date
     */
    public void setDateStart(Date DateStart);

    public void setDateEnd(Date DateEnd);

    public void setCountStrRead(int CountStrRead);

    public void setCountRecDB(int CountRecDB);

    public String getFileName();

    public void setFileName(String FileName);

    public void setCreateIndex(boolean CreateIndex);
    
    public void SaveReport();
    
}
