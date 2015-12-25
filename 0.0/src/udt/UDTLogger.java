/*
 *  Класс для логирования
 */
package udt;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author koshkin_as
 */
public class UDTLogger {
    
    private UDTLogger() {
    }
    
    private static Logger Log = null;
    private static ASettings MySettings;
    private String FileName = null;
    private boolean isOk = false;
    
    /**
     * Возвращает экземпляр класса.
     * Если запускается в первый раз, необходимо дополнительно вызвать Init();
     */
    public static UDTLogger getInstance() {
        
        return UDTLoggerHolder.INSTANCE;
    }
    
    private static class UDTLoggerHolder {

        private static final UDTLogger INSTANCE = new UDTLogger();
    }
       
    
    /**
     * Инициализация параметров 
     */
    public void Init(){
        MySettings = SettingsXML.getInstance();
        
        Date MyDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleFormatter SF = new SimpleFormatter();
                        
        if (MySettings.isLog()){
            Log = Logger.getLogger(UDT.class.getName());
            
            FileName = "Log_"+ sdf.format(MyDate)+ ".log";       
            
            try {         
                FileHandler FH = new FileHandler(MySettings.getPath_Log()+FileName);
                FH.setFormatter(SF);
                Log.addHandler(FH);            
                isOk = true;
            } catch (IOException ex) {
                Logger.getLogger(UDTLogger.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(UDTLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
        
    public void LogSend(String msg){              
        if (isOk) {
            Log.log(Level.OFF, msg);
        } else {
            Logger.getLogger(UDTLogger.class.getName()).log(Level.OFF, msg);
        }
        
    }
    
    public void LogSend(java.util.logging.Level level,String msg){        
        if (isOk) {
            Log.log(level, msg);
        } else {
            Logger.getLogger(UDTLogger.class.getName()).log(level, msg);
        }
    }
    
    
}
