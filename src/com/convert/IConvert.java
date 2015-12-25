/*
 * 
 * 
 * 
 */
package com.convert;

import com.Report.ReportData;
import com.task.ITask;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public interface IConvert {
    
    /**
     * Запуск процесса конвертации файла.
     * Под конвертацией понимается смена кодировки и дополнения управляющими символами
     * @return 
     * Возвращает true если 
     */
    
    public boolean Run();
    
    public void setTask(ITask task);
    
    public void setLogger(UDTLogger logger);
    
    public void setReportData(ReportData rpData);
}
