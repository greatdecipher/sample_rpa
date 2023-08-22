package com.albertsons.argus.datar2r.repo.oracle.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.datar2r.bo.oracle.custom.ProcessFileDetailsBO;
import com.albertsons.argus.datar2r.bo.oracle.custom.ProcessInstanceBO;
import com.albertsons.argus.datar2r.bo.oracle.custom.RecordBO;
import com.albertsons.argus.datar2r.repo.oracle.GetOracleTablesCustomRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class GetOracleTablesCustomRepoTest {

    @MockBean
    private GetOracleTablesCustomRepo mockRepo;

    @BeforeEach
    void setMockProcessFileDetailsBOs() {
        List<ProcessFileDetailsBO> list = new ArrayList<>();

        ProcessFileDetailsBO bo = new ProcessFileDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return 4835649L;
            }

            @Override
            public String getFile_Name() {
                return "";
            }

            @Override
            public String getFile_Size() {
                return "";
            }

            @Override
            public String getInterface_Id() {
                return "";
            }

            @Override
            public String getFile_Row_Count() {
                return "";
            }

            @Override
            public String getLoad_Row_Count() {
                return "";
            }

            @Override
            public String getLoad_Date_Time() {
                return "";
            }

            @Override
            public String getLoad_Status() {
                return "";
            }

            @Override
            public String getValidation_Failure_Row_Count() {
                return "";
            }

            @Override
            public String getValidation_Status() {
                return "";
            }

            @Override
            public String getValidation_Date_Time() {
                return "";
            }

            @Override
            public String getAttribute1() {
                return "";
            }

            @Override
            public String getAttribute2() {
                return "";
            }

            @Override
            public String getAttribute3() {
                return "";
            }

            @Override
            public String getAttribute4() {
                return "";
            }

            @Override
            public String getAttribute5() {
                return "";
            }

            @Override
            public String getAttribute6() {
                return "";
            }

            @Override
            public String getAttribute7() {
                return "";
            }

            @Override
            public String getAttribute8() {
                return "";
            }

            @Override
            public String getAttribute9() {
                return "";
            }

            @Override
            public String getAttribute10() {
                return "";
            }

            @Override
            public String getAttribute11() {
                return "";
            }

            @Override
            public String getAttribute12() {
                return "";
            }

            @Override
            public String getAttribute13() {
                return "";
            }

            @Override
            public String getAttribute14() {
                return "";
            }

            @Override
            public String getAttribute15() {
                return "";
            }

            @Override
            public String getProcess_Time() {
                return "";
            }

        };

        list.add(bo);

        when(mockRepo.getProcessFileDetailsBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockRecordBOs() {
        List<RecordBO> list = new ArrayList<>();

        RecordBO bo = new RecordBO(){

            @Override
            public Long getGroup_Id() {
                return null;
            }

            @Override
            public String getBatch_Name() {
                return "";
            }

            @Override
            public Long getTotal_Rec() {
                return 13866L;
            }

            @Override
            public Long getConsumed_Rec() {
                return 20799L;
            }

            @Override
            public Long getRemaining_Rec() {
                return 6933L;
            }

            @Override
            public String getSum_Entered_Dr() {
                return "";
            }

            @Override
            public String getSum_Entered_Cr() {
                return "";
            }

            @Override
            public String getMin_Process_Time() {
                return "";
            }

            @Override
            public String getMax_Process_Time() {
                return "";
            }
        };

        list.add(bo);

        when(mockRepo.getRecordBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockProcessInstanceBOs() {
        List<ProcessInstanceBO> list = new ArrayList<>();

        ProcessInstanceBO bo = new ProcessInstanceBO(){

            @Override
            public Long getInstance_Id() {
                return 1021649L;
            }

            @Override
            public String getIntegration_Name() {
                return "";
            }

            @Override
            public String getIntegration_Pattern() {
                return "";
            }

            @Override
            public String getRun_Date() {
                return "";
            }

            @Override
            public String getStart_Time() {
                return "";
            }

            @Override
            public String getEnd_Time() {
                return "";
            }

            @Override
            public String getStatus_Time() {
                return "";
            }

            @Override
            public String getAttribute1() {
                return "";
            }

            @Override
            public String getAttribute2() {
                return "";
            }

            @Override
            public String getAttribute3() {
                return "";
            }

            @Override
            public String getAttribute4() {
                return "";
            }

            @Override
            public String getAttribute5() {
                return "";
            }

            @Override
            public String getAttribute6() {
                return "";
            }

            @Override
            public String getAttribute7() {
                return "";
            }

            @Override
            public String getAttribute8() {
                return "";
            }

            @Override
            public String getAttribute9() {
                return "";
            }

            @Override
            public String getAttribute10() {
                return "";
            }

            @Override
            public String getAttribute11() {
                return "";
            }

            @Override
            public String getAttribute12() {
                return "";
            }

            @Override
            public String getAttribute13() {
                return "";
            }

            @Override
            public String getAttribute14() {
                return "";
            }

            @Override
            public String getAttribute15() {
                return "";
            }

            @Override
            public String getStatus() {
                return "";
            }

            @Override
            public String getAic_Instance_Id() {
                return "";
            }

            @Override
            public String getTransformation_Status() {
                return "";
            }

            @Override
            public String getStart_Batch() {
                return "";
            }

            @Override
            public String getEnd_Batch() {
                return "";
            }

            @Override
            public String getInterface_Id() {
                return "";
            }

            @Override
            public String getTransformation_Msg() {
                return "";
            }

            @Override
            public String getBatch_Name() {
                return "";
            }

            @Override
            public String getParent_Instance_Id() {
                return "";
            }

            @Override
            public String getProcess_Time() {
                return "";
            }

            

        };

        list.add(bo);

        when(mockRepo.getProcessInstanceBOs(anyString())).thenReturn(list);
    }

    @Test
    void testGetProcessFileDetailsBOs(){
        List<ProcessFileDetailsBO> boList = mockRepo.getProcessFileDetailsBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }

    @Test
    void testGetRecordBOs(){
        List<RecordBO> boList = mockRepo.getRecordBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }

    @Test
    void testGetProcessInstanceBOs(){
        List<ProcessInstanceBO> boList = mockRepo.getProcessInstanceBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
}

