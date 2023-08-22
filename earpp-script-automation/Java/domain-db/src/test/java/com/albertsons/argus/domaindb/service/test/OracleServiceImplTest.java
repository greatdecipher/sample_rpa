package com.albertsons.argus.domaindb.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.DomainDBApplication;
import com.albertsons.argus.data.bo.oracle.custom.OracleCountCustomBO;
import com.albertsons.argus.data.repo.oracle.CountOracleTablesCustomRepo;
import com.albertsons.argus.domaindb.dto.OracleDTO;
import com.albertsons.argus.domaindb.service.OracleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DomainDBApplication.class)
class OracleServiceImplTest{
    @Autowired
    private OracleService service;
    
    @MockBean
    private CountOracleTablesCustomRepo mockRepo;

    @BeforeEach
    void SetMockOutput() {

        List<OracleCountCustomBO> list1 = new ArrayList<>();
        List<OracleCountCustomBO> list2 = new ArrayList<>();

        //Scenario #1
        OracleCountCustomBO bo1 = new OracleCountCustomBO(){

            @Override
            public String getTableNm() {
                return "APINV";
            }

            @Override
            public Long getBoCount() {
                return 4835649L;
            }};

        list1.add(bo1);

        when(mockRepo.getOracleDBCountCustomBOs()).thenReturn(list1);

        //Scenario #2
        OracleCountCustomBO bo2 = new OracleCountCustomBO(){

            @Override
            public String getTableNm() {
                return "APPO";
            }

            @Override
            public Long getBoCount() {
                return 2817103L;
            }};

        list2.add(bo2);

        when(mockRepo.getOracleDBCountCustomBOs()).thenReturn(list2);

    }

    @Test
    void TestCountOracleTables() throws Exception {

        List<OracleDTO> dto1 = service.getOracleDTO();
        List<OracleDTO> dto2 = service.getOracleDTO();

        assertNotNull(dto1);
        assertEquals(1, dto1.size());
        assertEquals(1, dto2.size());

    }
}

    