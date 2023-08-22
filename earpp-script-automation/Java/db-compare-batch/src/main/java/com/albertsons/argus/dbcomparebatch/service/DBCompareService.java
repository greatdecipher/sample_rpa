package com.albertsons.argus.dbcomparebatch.service;

import java.util.List;

import com.albertsons.argus.domaindb.dto.DB2DTO;
import com.albertsons.argus.domaindb.dto.OracleDTO;
import com.albertsons.argus.dbcomparebatch.exception.DBCompareException;

public interface DBCompareService {
    static final Integer HIGH_PRIORITY = 1;
    static final Integer NORMAL_PRIORITY = 3; 

    public void getOracleAndDB2Rows(List<String> headers, List<OracleDTO> oracleDTOs, List<DB2DTO> db2DTOs) throws DBCompareException;

    public void sendDBCompareEmail(List<String> headers, List<String[]> rows);

    public String toDBCompareHTMLString(List<String> headers, List<String[]> rows);

    public List<String[]> prepareRows(List<OracleDTO> oracleDTOs, List<DB2DTO> db2DTOs);

    public List<String[]> sortByColumn(String arr[][], int col);

    public void writeToCSV(String filepath, List<String> headers, List<String[]> rows);

    public void deleteAttachment(String filepath);

    public List<String> getHeadersList();

    public void moveLogFile();
   
}
