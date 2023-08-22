package com.albertsons.argus.domaindb.service.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.DomainDBApplication;
import com.albertsons.argus.data.bo.oracle.custom.OracleCountCustomBO;
import com.albertsons.argus.data.repo.oracle.CountOracleTablesCustomRepo;
import com.albertsons.argus.domaindb.dto.DB2DTO;
import com.albertsons.argus.domaindb.dto.OracleDTO;
import com.albertsons.argus.domaindb.service.DB2Service;
import com.albertsons.argus.domaindb.service.OracleService;
import com.albertsons.argus.data.bo.db2.custom.DB2CountCustomBO;
import com.albertsons.argus.data.repo.db2.CountDB2TablesCustomRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DomainDBApplication.class)
class CountTablesServiceImplTest {
    @Autowired
    private OracleService oracleService;

    @Autowired
    private DB2Service db2Service;
    
    @MockBean
    private CountOracleTablesCustomRepo mockOracleRepo;

    @MockBean
    private CountDB2TablesCustomRepo mockDB2Repo;

    @BeforeEach
    void SetMockOutput() {

        List<OracleCountCustomBO> oracleList = new ArrayList<>();
        List<DB2CountCustomBO> db2List = new ArrayList<>();

        //Scenario #1 - Equal
        OracleCountCustomBO oracleBo1 = new OracleCountCustomBO(){

            @Override
            public String getTableNm() {
                return "APINV";
            }

            @Override
            public Long getBoCount() {
                return 100L;
            }

        };

        oracleList.add(oracleBo1);

        when(mockOracleRepo.getOracleDBCountCustomBOs()).thenReturn(oracleList);

        DB2CountCustomBO db2Bo1 = new DB2CountCustomBO(){

            @Override
            public String getTableNm() {
                return "APINV";
            }

            @Override
            public Long getBoCount() {
                return 100L;
            }

        };

        db2List.add(db2Bo1);

        when(mockDB2Repo.getDB2DBCountCustomBOs()).thenReturn(db2List);


        //Scenario #2 - Not Equal
        OracleCountCustomBO oracleBo2 = new OracleCountCustomBO(){

            @Override
            public String getTableNm() {
                return "APPO";
            }

            @Override
            public Long getBoCount() {
                return 500L;
            }
        
        };

        oracleList.add(oracleBo2);

        when(mockOracleRepo.getOracleDBCountCustomBOs()).thenReturn(oracleList);

        DB2CountCustomBO db2Bo2 = new DB2CountCustomBO(){

            @Override
            public String getTableNm() {
                return "APPO";
            }

            @Override
            public Long getBoCount() {
                return 800L;
            }

        };

        db2List.add(db2Bo2);

        when(mockDB2Repo.getDB2DBCountCustomBOs()).thenReturn(db2List);

    }


    @Test
    void TestCountTables() throws Exception {

        OracleDTO oracleDTOs1 = oracleService.getOracleDTO().get(0);
        OracleDTO oracleDTOs2 = oracleService.getOracleDTO().get(1);
        DB2DTO db2DTOs1 = db2Service.getDB2DTO().get(0);
        DB2DTO db2DTOs2 = db2Service.getDB2DTO().get(1);

        assertNotNull(oracleDTOs1);
        assertNotNull(oracleDTOs2);
        assertNotNull(db2DTOs1);
        assertNotNull(db2DTOs2);

        assertTrue(oracleDTOs1.getRowCount() == db2DTOs1.getRowCount(), "Number of rows of " + oracleDTOs1.getTableName() + " in Oracle and " + db2DTOs1.getTableName() + " in DB2 are equal");
        assertFalse(oracleDTOs2.getRowCount() == db2DTOs2.getRowCount(), "Number of rows of " + oracleDTOs2.getTableName() + " in Oracle and " + db2DTOs1.getTableName() + " in DB2 are not equal");
    }
}
