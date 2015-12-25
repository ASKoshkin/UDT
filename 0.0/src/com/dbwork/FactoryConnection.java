/*
 * 
 * 
 * 
 */
package com.dbwork;

import udt.ISettings;
import udt.ISettings.TableEnum;
import udt.SettingsXML;

/**
 *
 * @author koshkin_as
 */
public class FactoryConnection extends AFactoryConnection {
    
    private FactoryConnection() {
    }
    
    public static FactoryConnection getInstance() {
        return FactoryConnectionHolder.INSTANCE;
    }

    private static class FactoryConnectionHolder {

        private static final FactoryConnection INSTANCE = new FactoryConnection();
    }
    
    
    @Override
    public IConnection getConnection() {
        IConnection obj = null;
        ISettings MySettings = SettingsXML.getInstance();
        
        if (MySettings.getTableUpdate()==TableEnum.Passport){
            obj = ConnectionPASSPORT.getInstance();
        } else if(MySettings.getTableUpdate()==TableEnum.Ofac){
            obj = ConnectionOFAC.getInstance();
        }
        
        return obj;
    }
    
    
}
