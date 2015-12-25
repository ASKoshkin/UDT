/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convert;

import udt.ISettings;
import udt.ISettings.TableEnum;
import udt.SettingsXML;

/**
 *
 * @author koshkin_as
 */
public class FactoryConvert extends AFactoryConvert{
    
    private FactoryConvert() {
    }
    
    public static FactoryConvert getInstance() {
        return FactoryConvertHolder.INSTANCE;
    }

    private static class FactoryConvertHolder {

        private static final FactoryConvert INSTANCE = new FactoryConvert();
    }
    
    
    @Override
    public IConvert getConvert() {
        IConvert obj = null;
        
        ISettings MySettings = SettingsXML.getInstance();
        
        if (MySettings.getTableUpdate()==TableEnum.Passport){
            
            obj = ConvertPASSPORT.getInstance();
            
        } else if (MySettings.getTableUpdate()==TableEnum.Ofac){
            obj = ConvertOFAC.getInstance();
        }
        
        return obj;        
    }
}
