package com.albertsons.argus.dbcomparebatch.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.DBCompareBatchTestApplication;
import com.albertsons.argus.domaindb.dto.DB2DTO;
import com.albertsons.argus.domaindb.dto.OracleDTO;
import com.albertsons.argus.domaindb.service.OracleService;
import com.albertsons.argus.dbcomparebatch.exception.DBCompareException;
import com.albertsons.argus.dbcomparebatch.service.DBCompareService;
import com.albertsons.argus.domaindb.exception.DB2ServiceException;
import com.albertsons.argus.domaindb.exception.OracleServiceException;
import com.albertsons.argus.domaindb.service.DB2Service;
import com.albertsons.argus.mail.service.EmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DBCompareBatchTestApplication.class)
class DBCompareServiceTest {

    @Autowired
    private DBCompareService dbCompareService;

    @MockBean
    private EmailService mockEmailService;

    @MockBean
    private OracleService mockOracleService;
    
    @MockBean
    private DB2Service mockDB2Service;

    @BeforeEach
    void setMockOutput(){
        //set headers
        List<String> headers = new ArrayList<>();
        
        headers.add("DB2 Table Name (Mainframe Side)");
        headers.add("DB2 Row Count");
        headers.add("Oracle RDS SSIMS Table Name (EDIS Side - ATLAS)");
        headers.add("Oracle Row Count");
        headers.add("Variance (Oracle count - Db2 count)");
        headers.add("Variance Percentage");

        //set DTOs
        List<OracleDTO> oracleDTOs = new ArrayList<>();
        List<DB2DTO> db2DTOs = new ArrayList<>();

        OracleDTO oracleDTO = new OracleDTO();
        DB2DTO db2DTO = new DB2DTO();

        oracleDTO.setTableName("APINV");
        oracleDTO.setRowCount(100L);
        db2DTO.setTableName("APINV");
        db2DTO.setRowCount(100L);

        oracleDTOs.add(oracleDTO);
        db2DTOs.add(db2DTO);

        //set sorted rows
        List<String[]> rows = new ArrayList<>();

        String rowResult[] = {  oracleDTO.getTableName(), 
                                oracleDTO.getRowCount().toString(), 
                                db2DTO.getTableName(), 
                                db2DTO.getRowCount().toString(),
                                "0",
                                "0"
                             };

        rows.add(rowResult);

        try {
            when(mockOracleService.getOracleDTO()).thenReturn(oracleDTOs);
            when(mockDB2Service.getDB2DTO()).thenReturn(db2DTOs);
            
            doNothing().when(dbCompareService).getOracleAndDB2Rows(any(), any(), any());
            when(dbCompareService.prepareRows(anyList(), anyList())).thenReturn(rows);
            when(dbCompareService.sortByColumn(any(), anyInt())).thenReturn(rows);
            doNothing().when(dbCompareService).writeToCSV(anyString(), headers, rows);
            doNothing().when(dbCompareService).deleteAttachment(anyString());
            when(dbCompareService.getHeadersList()).thenReturn(headers);
            doNothing().when(dbCompareService).moveLogFile();
        
            doNothing().when(mockEmailService).sendMessageWithAttachment(anyString(), anyString(), any(String[].class), any(String[].class), anyString(), anyString(), null, anyInt(), anyBoolean());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetOracleAndDB2Rows() throws DBCompareException {
        try {
            dbCompareService.getOracleAndDB2Rows(dbCompareService.getHeadersList(), mockOracleService.getOracleDTO(), mockDB2Service.getDB2DTO());

            assertNotNull(mockOracleService.getOracleDTO());
            assertNotNull(mockDB2Service.getDB2DTO());

            assertEquals(mockDB2Service.getDB2DTO().get(0).getRowCount(), mockOracleService.getOracleDTO().get(0).getRowCount());
        } catch (OracleServiceException | DB2ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSendDBCompareEmail() {
        try {
            dbCompareService.sendDBCompareEmail(dbCompareService.getHeadersList(), dbCompareService.prepareRows(mockOracleService.getOracleDTO(), mockDB2Service.getDB2DTO()));
        
            assertNotNull(dbCompareService.getHeadersList());
            assertNotNull(dbCompareService.prepareRows(mockOracleService.getOracleDTO(), mockDB2Service.getDB2DTO()));
        } catch (OracleServiceException | DB2ServiceException e){
            e.printStackTrace();
        }

    }

    @Test
    void testToDBCompareHTMLString() {
        try {
            String body = dbCompareService.toDBCompareHTMLString(dbCompareService.getHeadersList(), dbCompareService.prepareRows(mockOracleService.getOracleDTO(), mockDB2Service.getDB2DTO()));
            assertNotNull(body);
        } catch (OracleServiceException | DB2ServiceException e){
            e.printStackTrace();
        }
    }

    @Test
    void testPrepareRows(){
        try {
            List<String[]> rows = dbCompareService.prepareRows(mockOracleService.getOracleDTO(), mockDB2Service.getDB2DTO());
            assertNotNull(rows);
        } catch (OracleServiceException | DB2ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSortByColumn(){
        String[][] listToArr = new String[1][1];

        List<String[]> rows = dbCompareService.sortByColumn(listToArr, 5);
        assertNotNull(rows);
    }

    @Test
    void testWriteToCSV(){
        String attachment = "C:\\Automations\\DB_Compare\\DBCompare.csv";
        try {
            dbCompareService.writeToCSV(attachment, dbCompareService.getHeadersList(), dbCompareService.prepareRows(mockOracleService.getOracleDTO(), mockDB2Service.getDB2DTO()));
            
            assertTrue(attachment.contains("DBCompare.csv"));
            assertNotNull(dbCompareService.prepareRows(mockOracleService.getOracleDTO(), mockDB2Service.getDB2DTO()));
        } catch (OracleServiceException | DB2ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDeleteAttachment(){
        String attachment = "C:\\Automations\\DB_Compare\\DBCompare.csv";
        dbCompareService.deleteAttachment(attachment);

        assertTrue(attachment.contains("DBCompare.csv"));
    }

    @Test
    void testGetHeadersListTest(){
        List<String> headers = dbCompareService.getHeadersList();
        assertNotNull(headers);
    }

    @Test
    void testGetOracleAndDB2Rows_Exception() throws Exception {
        when(mockOracleService.getOracleDTO()).thenThrow(new OracleServiceException("Problem retrieving Oracle DTOs", new NullPointerException()));
        when(mockDB2Service.getDB2DTO()).thenThrow(new DB2ServiceException("Problem retrieving DB2 DTOs", new NullPointerException()));

        OracleServiceException dse1 = assertThrows(OracleServiceException.class, () -> {
            mockOracleService.getOracleDTO();
        });

        DB2ServiceException dse2 = assertThrows(DB2ServiceException.class, () -> {
            mockDB2Service.getDB2DTO();
        });

        String expectedOracleMessage = "Problem retrieving Oracle DTOs";
        String expectedDB2Message = "Problem retrieving DB2 DTOs";
        String oracleMessage = dse1.getMessage();
        String db2Message = dse2.getMessage();

        assertTrue(oracleMessage.contains(expectedOracleMessage));
        assertTrue(db2Message.contains(expectedDB2Message));
    }

}
