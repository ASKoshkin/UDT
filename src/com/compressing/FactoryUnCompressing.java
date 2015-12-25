/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compressing;

import com.task.ITask;
import udt.UDTLogger;

/**
 *
 * @author Alexey
 */
public class FactoryUnCompressing {
    private FactoryUnCompressing() {
    }
    
    public static synchronized FactoryUnCompressing getInstance() {
        return FactoryUnCompressingHolder.INSTANCE;
    }
    
    private static class FactoryUnCompressingHolder {

        private static final FactoryUnCompressing INSTANCE = new FactoryUnCompressing();
    }
    
    public synchronized IUnCompressing getUnCompressing(ITask task, UDTLogger logger){
        IUnCompressing ucmpr = null;
        
        if (task.getName().toUpperCase().equals("PASSPORT")){
            ucmpr = UnCompressingRAR.getInstance();
            ucmpr.setTask(task);
        }
        
        if (ucmpr != null){
            ucmpr.setLogger(logger);
        }
        
        return ucmpr;
    }
    
}
