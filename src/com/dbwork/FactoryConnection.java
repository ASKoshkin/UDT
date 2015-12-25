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
public class FactoryConnection extends AFactoryConnection {
    
    private FactoryConnection() {
    }
    
    public synchronized static FactoryConnection getInstance() {
        return FactoryConnectionHolder.INSTANCE;
    }

    private static class FactoryConnectionHolder {

        private static final FactoryConnection INSTANCE = new FactoryConnection();
    }
    
    @Override
    public synchronized IConnection getConnection(ITask task, UDTLogger logger, ReportData rpData) {
        IConnection obj = null;
        
        if (task.getName().toUpperCase().equals("PASSPORT")){
            obj = ConnectionPASSPORT.getInstance();
        } else if(task.getName().toUpperCase().equals("OFAC")){
            obj = ConnectionOFAC.getInstance();
        }
        
        if (obj != null){
            obj.setTask(task);
            obj.setLogger(logger);
            obj.setReportData(rpData);
        }
        
        return obj;
    }
    
}
