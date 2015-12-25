/*
 *  Класс для логирования
 */
package udt;

import com.task.ITask;
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
    
    private Logger Log;
    private ASettings MySettings;
    private String FileName = null;
    private boolean isOk = false;
    private ITask task;

    public UDTLogger() {
        this.Log = null;
    }
    

    public void setTask(ITask task) {
        this.task = task;
    }
    
    
    /**
     * Инициализация параметров 
     */
    public void Init(){
        MySettings = SettingsXML.getInstance();
        
        Date MyDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
        SimpleFormatter SF = new SimpleFormatter();
                        
        if (MySettings.isLog()){
            Log = Logger.getLogger(this.toString());
            FileName = task.getName()+"_"+ sdf.format(MyDate)+ ".log";       
            
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
