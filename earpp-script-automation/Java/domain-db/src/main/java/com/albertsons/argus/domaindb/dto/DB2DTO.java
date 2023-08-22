package com.albertsons.argus.domaindb.dto;

public class DB2DTO {
    private String tableName;
    private Long rowCount;

    public DB2DTO(){

    }

    public DB2DTO(String tableName, Long rowCount){
        this.tableName = tableName;
        this.rowCount = rowCount;
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public String getTableName(){
        return tableName;
    }

    public void setRowCount(Long rowCount){
        this.rowCount = rowCount;
    }

    public Long getRowCount(){
        return rowCount;
    }
}
