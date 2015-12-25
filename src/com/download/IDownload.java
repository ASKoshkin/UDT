/*
 * 
 * Интерфейс для класса Download - загрузки файла 
 * 
 */
package com.download;

import com.task.ITask;
import udt.UDTLogger;

/**
 *
 * @author koshkin_as
 */

public interface IDownload {
    /**
     * 
     * @return 
     * Возращает true если файлы скачался без ошибок и размер файла совпадает с 
     * файлом на сервере.
     */
    public boolean run();
    
    /**
     * Передаем задачу экземпляру класса
     * @param task типа ITask
     */
    public void setTask(ITask task);
    
    /**
     * 
     */
    public void setLogger(UDTLogger logger);
    
    
}
