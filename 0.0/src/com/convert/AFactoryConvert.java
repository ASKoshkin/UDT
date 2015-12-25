/*
 * 
 * 
 * 
 */
package com.convert;

/**
 *
 * @author koshkin_as
 */
public abstract class AFactoryConvert {
    
    /**
     * Метод возвращает экземпляр класса.
     * Выбор класса зависит от того какую таблицу мы обновляем
     * @return 
     * Возвращает объект IConvert
     */
    public abstract IConvert getConvert();
    
}
