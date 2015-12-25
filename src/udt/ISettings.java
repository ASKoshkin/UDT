/**
 * 
 */
package udt;

import com.task.ITask;
import java.util.Queue;

/**
 *
 * @author koshkin_as
 */
public interface ISettings {
        
    /**
     * Чтение файла настроек.
     * Данный метод абстрактный, возможно реализовать чтение из других форматов
     * 
     * @return При успешном чтение файла возвращает true иначе false
     */
    public boolean ReadSettings();
    
    
    public boolean isLog();

    public String getPath_Log();

    public String getProxyHost();

    public String getProxyPort();
    
    public String getBCP();
    
    /**
     * Возвращает очередь задач
     * @return Возвращает очередь
     */
    public Queue<ITask> getTaskQue();

    
    // Какую таблицу обновляем 
    public enum TableEnum{
        Passport 
        ,Ofac    
    }
    
}