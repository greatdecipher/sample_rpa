package com.albertsons.argus.data.repo.oracle.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.data.bo.oracle.custom.OracleCountCustomBO;
import com.albertsons.argus.data.repo.oracle.CountOracleTablesCustomRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CountOracleTablesCustomRepoTest {

    @MockBean
    private CountOracleTablesCustomRepo mockRepo;

    @BeforeEach
    void setMockOutput() {
        List<OracleCountCustomBO> list = new ArrayList<>();

        OracleCountCustomBO bo = new OracleCountCustomBO(){

            @Override
            public String getTableNm() {
                return "APINV";
            }

            @Override
            public Long getBoCount() {
                return 4835649L;
            }};

        list.add(bo);

        when(mockRepo.getOracleDBCountCustomBOs()).thenReturn(list);
    }

    @Test
    void testGetDBCountCustomBOs(){
        List<OracleCountCustomBO> boCountList = mockRepo.getOracleDBCountCustomBOs();
        
        assertTrue(boCountList.size() > 0, "List is greater than 0");
    }
}

