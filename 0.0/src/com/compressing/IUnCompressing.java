/*
 * Распаковка архива
 * 
 */
package com.compressing;

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
    
}
