/*
 * Абстрактый класс. 
 * оставлю возможность считывать файл нестроек не только с xml но и с других форматов
 * для этого метод ReadFile сделаю абстрактным.
 */


package udt;

import com.task.TaskImpl;
import java.util.Queue;

/**
 *
 * @author koshkin_as
 */
public abstract class ASettings implements ISettings{
        
    protected boolean Log;
    protected String Path_Log;
    
    protected String ProxyHost;
    protected String ProxyPort;
    protected String BCP;
    
    protected Queue<TaskImpl> TaskQueue;
    
    
    
    @Override
    public abstract boolean ReadSettings();
    
    /**
     * 
     * @return Возвращает true если файл существует, иначе false
     * 
     * 
     */
    public abstract boolean CheckFile();
    

    @Override
    public synchronized boolean isLog() {
        return Log;
    }

    @Override
    public synchronized String getPath_Log() {
        return Path_Log;
    }

    @Override
    public synchronized String getProxyHost() {
        return ProxyHost;
    }

    @Override
    public synchronized String getProxyPort() {
        return ProxyPort;
    }

    @Override
    public synchronized String getBCP() {
        return BCP;
    }
    
    
   

}
