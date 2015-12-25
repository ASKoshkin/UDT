/*
 * 
 * 
 * 
 */
package com.dbwork;

import java.sql.SQLException;

/**
 *
 * @author koshkin_as
 */

public interface IConnection {
    
    /**
     * Запуск работы с БД
     * @return 
     * Возвращает true если процесс завершился успешно иначе false
     */
    public boolean Run();
    
    /**
     * Удаление индекса
     * @return 
     * Возвращает true если процесс завершился успешно иначе false
     */
    public boolean DropIndex() throws SQLException;
    
    /**
     * Создание индекса
     * @return 
     * Возвращает true если процесс завершился успешно иначе false
     */
    public boolean CreateIndex();
    
    /**
     * Возвращает количество строк в таблице
     * @return 
     * Возвращаемый тип int
     */
    public int getCountRecord();
    
    /**
     * Закрывает доступ к БД
     */
    public void CloseConnectionDB();
    
    /**
     * Запуск bcp.exe - импорт данных в бд
    */
    public boolean BCPRun();
    
}
