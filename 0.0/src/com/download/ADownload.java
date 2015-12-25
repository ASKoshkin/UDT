/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.download;

/**
 *
 * @author koshkin_as
 */
public abstract class ADownload implements IDownload {
    
    /**
     * Метод запуска скачаивания файла.
     * @return 
     * Возращает true если файлы скачался без ошибок и размер файла совпадает с 
     * файлом на сервере.
     */
    @Override
    public abstract boolean run();
    
        
    
}
