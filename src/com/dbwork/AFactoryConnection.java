/*
 * 
 * 
 * 
 */
package com.dbwork;

import com.Report.ReportData;
import com.task.ITask;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public abstract class AFactoryConnection {
    /**
     * Возвращает сылку на один из экземпляров класса
     * @return 
     * Возвращаемый тип IConnection
     */
    public abstract IConnection getConnection(ITask task, UDTLogger logger, ReportData rpData);
    
}
