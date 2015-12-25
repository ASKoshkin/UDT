/*
 * Распаковка архива
 * 
 */
package com.compressing;

import com.task.ITask;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */
public interface IUnCompressing {
    
    /**
     * Запуск процесса распаковки файла
     * @return 
     * Возвращает true если процесс завершился удачно, иначе false
     */
    public boolean Run();
    
    public void setTask(ITask task);
    
    public void setLogger(UDTLogger logger);
    
}
