package com.albertsons.argus.domaindb.dto;

public class OracleDTO {
    private String tableName;
    private Long rowCount;

    public OracleDTO(){

    }

    public OracleDTO(String tableName, Long rowCount){
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
