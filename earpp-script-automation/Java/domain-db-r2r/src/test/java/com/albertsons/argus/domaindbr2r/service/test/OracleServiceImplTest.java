package com.albertsons.argus.domaindbr2r.service.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.DomainDBR2RApplication;
import com.albertsons.argus.domaindbr2r.dto.ProcessFileDetailsDTO;
import com.albertsons.argus.domaindbr2r.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbr2r.dto.RecordDTO;
import com.albertsons.argus.domaindbr2r.exception.OracleServiceException;
import com.albertsons.argus.domaindbr2r.service.R2ROracleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DomainDBR2RApplication.class)
class OracleServiceImplTest{
    
    @MockBean
    private R2ROracleService mockOracleService;

    @BeforeEach
    void setProcessFileDetailsMockOutput() {
        List<ProcessFileDetailsDTO> list = new ArrayList<>();

        ProcessFileDetailsDTO dto = new ProcessFileDetailsDTO();
        dto.setInstance_Id(70000000L);
        dto.setFile_Name("JE_JIF_SC_20220101231111.dat");
        dto.setFile_Size("1");
        dto.setInterface_Id("INT46");
        dto.setFile_Row_Count(null);
        dto.setLoad_Row_Count(null);
        dto.setLoad_Date_Time(null);
        dto.setLoad_Status("TRANSFORMED_NOVAL");
        dto.setValidation_Failure_Row_Count(null);
        dto.setValidation_Status(null);
        dto.setValidation_Date_Time(null);
        dto.setAttribute1(null);
        dto.setAttribute2(null);
        dto.setAttribute3(null);
        dto.setAttribute4(null);
        dto.setAttribute5(null);
        dto.setAttribute6(null);
        dto.setAttribute7(null);
        dto.setAttribute8(null);
        dto.setAttribute9(null);
        dto.setAttribute10(null);
        dto.setAttribute11(null);
        dto.setAttribute12(null);
        dto.setAttribute13(null);
        dto.setAttribute14(null);
        dto.setAttribute15(null);
        dto.setProcess_Time("12-MAY-22 11.37.03.531805000 AM");

        list.add(dto);

        try {
            when(mockOracleService.getProcessFileDetailsDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setProcessInstanceMockOutput(){
        List<ProcessInstanceDTO> list = new ArrayList<>();

        ProcessInstanceDTO piDTO = new ProcessInstanceDTO();
		piDTO = new ProcessInstanceDTO();
		piDTO.setInstance_Id(90000000L);
		piDTO.setIntegration_Name("Journal Entries to Oracle Cloud GL");
		piDTO.setIntegration_Pattern("Inbound");
		piDTO.setRun_Date(null);
		piDTO.setStart_Time(null);
		piDTO.setEnd_Time(null);
		piDTO.setStatus_Time(null);
		piDTO.setAttribute1(null);
		piDTO.setAttribute2(null);
		piDTO.setAttribute3(null);
		piDTO.setAttribute4(null);
		piDTO.setAttribute5(null);
		piDTO.setAttribute6(null);
		piDTO.setAttribute7(null);
		piDTO.setAttribute8(null);
		piDTO.setAttribute9(null);
		piDTO.setAttribute10(null);
		piDTO.setAttribute11(null);
		piDTO.setAttribute12(null);
		piDTO.setAttribute13(null);
		piDTO.setAttribute14(null);
		piDTO.setAttribute15(null);
		piDTO.setStatus(null);
		piDTO.setAic_Instance_Id(null);
		piDTO.setTransformation_Status("TRANSFORMED_ERP");
		piDTO.setStart_Batch(null);
		piDTO.setEnd_Batch(null);
		piDTO.setInterface_Id("INT46");
		piDTO.setTransformation_Msg(null);
		piDTO.setBatch_Name("JE_JIF_SC_20220101000000.dat");
		piDTO.setParent_Instance_Id(null);
		piDTO.setProcess_Time(null);

        list.add(piDTO);

        try {
            when(mockOracleService.getProcessInstanceDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setRecordMockOutput(){
        List<RecordDTO> list = new ArrayList<>();

        RecordDTO recordDTO = new RecordDTO();
		recordDTO.setGroup_Id(20220508201819L);
		recordDTO.setBatch_Name("JE_JIF_SC_20220101000000.dat");
		recordDTO.setTotal_Rec(20799L);
		recordDTO.setConsumed_Rec(20799L);
		recordDTO.setRemaining_Rec(0L);
		recordDTO.setSum_Entered_Dr(null);
		recordDTO.setSum_Entered_Cr(null);
		recordDTO.setMin_Process_Time(null);
		recordDTO.setMax_Process_Time(null);

        list.add(recordDTO);

        try {
            when(mockOracleService.getRecordDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetProcessFileDetailsDTOs() throws OracleServiceException {
        List<ProcessFileDetailsDTO> dto = mockOracleService.getProcessFileDetailsDTOs("JE_JIF_SC_20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetProcessInstanceDTOs() throws OracleServiceException {
        List<ProcessInstanceDTO> dto = mockOracleService.getProcessInstanceDTOs("JE_JIF_SC_20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetRecordDTOs() throws OracleServiceException {
        List<RecordDTO> dto = mockOracleService.getRecordDTOs("JE_JIF_SC_20220101231111.dat");
        assertNotNull(dto);
    }
    
}

    