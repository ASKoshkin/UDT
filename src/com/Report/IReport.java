/**
 * Интерфейс класса отчета
 * 
 */
package com.Report;

import java.util.Date;


/**
 *
 * @author koshkin_as
 */
public interface IReport {

    public void setDateStart(Date DateStart);
   
    public void SaveReport(ReportData data);
    
}
