/*
 * 
 * 
 * 
 */
package com.dbwork;

/**
 *
 * @author koshkin_as
 */
public abstract class AFactoryConnection {
    /**
     * Возвращает сылку на один из экземпляров класса
     * @return 
     * Возвращаемый тип IConnection
     */
    public abstract IConnection getConnection();
}
