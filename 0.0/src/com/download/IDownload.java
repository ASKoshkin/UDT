/*
 * 
 * Интерфейс для класса Download - загрузки файла 
 * 
 */
package com.download;

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
    
    
}
