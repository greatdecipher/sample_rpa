package com.albertsons.argus.data.repo.db2.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.data.bo.db2.custom.DB2CountCustomBO;
import com.albertsons.argus.data.repo.db2.CountDB2TablesCustomRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CountDB2TablesCustomRepoTest {
    
    @MockBean
    private CountDB2TablesCustomRepo mockRepo;

    @BeforeEach
    void setMockOutput() {
        List<DB2CountCustomBO> list = new ArrayList<>();
        
        DB2CountCustomBO bo = new DB2CountCustomBO(){

            @Override
            public String getTableNm() {
                return "APINV";
            }

            @Override
            public Long getBoCount() {
                return 4835649L;
            }};

        list.add(bo);

        when(mockRepo.getDB2DBCountCustomBOs()).thenReturn(list);
    }

    @Test
    void testGetDBCountCustomBOs(){
        List<DB2CountCustomBO> boCountList = mockRepo.getDB2DBCountCustomBOs();
        
        assertTrue(boCountList.size() > 0, "List is greater than 0");
    }
}