/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convert;

import com.Report.ReportData;
import com.task.ITask;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public class FactoryConvert {
    
    private FactoryConvert() {
    }
    
    public synchronized static FactoryConvert getInstance() {
        return FactoryConvertHolder.INSTANCE;
    }

    private static class FactoryConvertHolder {

        private static final FactoryConvert INSTANCE = new FactoryConvert();
    }

    
    public synchronized IConvert getConvert(ITask task, UDTLogger logger, ReportData rpData) {
        IConvert obj = null; 
        
        if (task.getName().toUpperCase().equals("PASSPORT")){
            
            obj = ConvertPASSPORT.getInstance();
            
        } else if (task.getName().toUpperCase().equals("OFAC")){
            obj = ConvertOFAC.getInstance();
        }
        
        if (obj !=null){
            obj.setLogger(logger);
            obj.setTask(task);
            obj.setReportData(rpData);
        }
        
        return obj;        
    }
}
