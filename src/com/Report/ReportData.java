/*
 * Класс описывает данные данные для отчета.
 * 
 */
package com.Report;

/**
 *
 * @author Alexey
 */
public class ReportData {
    
    /**
     * Количество конвертированных строк
     */
    private int     CountStrRead; 
    /**
     * Количество строк в БД
     */
    private int     CountRecDB;
    private String  FileName;
    private boolean CreateIndex;

    public ReportData() {
        CreateIndex = false;
    }

    public int getCountStrRead() {
        return CountStrRead;
    }

    public int getCountRecDB() {
        return CountRecDB;
    }

    public String getFileName() {
        return FileName;
    }

    public boolean isCreateIndex() {
        return CreateIndex;
    }

    public void setCountStrRead(int CountStrRead) {
        this.CountStrRead = CountStrRead;
    }

    public void setCountRecDB(int CountRecDB) {
        this.CountRecDB = CountRecDB;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public void setCreateIndex(boolean CreateIndex) {
        this.CreateIndex = CreateIndex;
    }
    
    
    
    
}
