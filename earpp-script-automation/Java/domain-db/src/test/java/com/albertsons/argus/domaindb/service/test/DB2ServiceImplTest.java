package com.albertsons.argus.domaindb.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.DomainDBApplication;
import com.albertsons.argus.data.bo.db2.custom.DB2CountCustomBO;
import com.albertsons.argus.data.repo.db2.CountDB2TablesCustomRepo;
import com.albertsons.argus.domaindb.dto.DB2DTO;
import com.albertsons.argus.domaindb.service.DB2Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DomainDBApplication.class)
class DB2ServiceImplTest{
    @Autowired
    private DB2Service service;
    
    @MockBean
    private CountDB2TablesCustomRepo mockRepo;

    @BeforeEach
    void SetMockOutput() {

        List<DB2CountCustomBO> list1 = new ArrayList<>();
        List<DB2CountCustomBO> list2 = new ArrayList<>();

        //Scenario #1
        DB2CountCustomBO bo1 = new DB2CountCustomBO(){

            @Override
            public String getTableNm() {
                return "APINV";
            }

            @Override
            public Long getBoCount() {
                return 4835649L;
            }};

        list1.add(bo1);

        when(mockRepo.getDB2DBCountCustomBOs()).thenReturn(list1);

        //Scenario #2
        DB2CountCustomBO bo2 = new DB2CountCustomBO(){

            @Override
            public String getTableNm() {
                return "APPO";
            }

            @Override
            public Long getBoCount() {
                return 2810103L;
            }};

        list2.add(bo2);

        when(mockRepo.getDB2DBCountCustomBOs()).thenReturn(list2);

    }

    @Test
    void TestCountDB2Tables() throws Exception {

        List<DB2DTO> dto1 = service.getDB2DTO();
        List<DB2DTO> dto2 = service.getDB2DTO();

        assertNotNull(dto1);
        assertEquals(1, dto1.size());
        assertEquals(1, dto2.size());

    }
}
