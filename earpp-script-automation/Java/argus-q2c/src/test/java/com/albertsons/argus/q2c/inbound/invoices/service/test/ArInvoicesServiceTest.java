package com.albertsons.argus.q2c.inbound.invoices.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.albertsons.argus.ArgusQ2CApplicationTest;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domaindbq2c.dto.InterfaceLinesDTO;
import com.albertsons.argus.domaindbq2c.dto.JobHistoryDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessFileDetailsDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbq2c.exception.OracleServiceException;
import com.albertsons.argus.domaindbq2c.service.DbOracleService;
import com.albertsons.argus.q2c.exception.Q2cInsuranceArException;
import com.albertsons.argus.q2c.inbound.invoices.service.ArInvoicesService;

/**
 * @author kbuen03
 * @since 01/25/23
 * @version 1.0
 * 
 */
@SpringBootTest(classes = ArgusQ2CApplicationTest.class)
@ActiveProfiles("test")
public class ArInvoicesServiceTest {
    @Autowired
    private ArInvoicesService arInvoicesService;

    @MockBean
    private DbOracleService dbOracleServiceMock;

    @MockBean
    private PlaywrightAutomationService playwrightAutomationServiceMock;

    @BeforeEach
    void setMockOutput() {

    }

    @Test
    void testGetProcessInstance() {
        List<ProcessInstanceDTO> processInstanceMockList = new ArrayList<>();

        ProcessInstanceDTO dto = getProcessInstanceDto();

        processInstanceMockList.add(dto);

        try {
            when(dbOracleServiceMock.getProcessInstanceByDescDTOs(anyString())).thenReturn(processInstanceMockList);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }

        List<ProcessInstanceDTO> instanceDTOs = new ArrayList<>();

        try {
            instanceDTOs = arInvoicesService.getProcessInstance("test.txt","121323456");
        } catch (Q2cInsuranceArException e) {
            e.printStackTrace();
        }

        assertNotNull(instanceDTOs);
        assertEquals(1, instanceDTOs.size());
    }

    @Test
    void testGetProcessInstance_noRecordException() {
        List<ProcessInstanceDTO> processInstanceMockList = new ArrayList<>();

        try {
            when(dbOracleServiceMock.getProcessInstanceDTOs(anyString())).thenReturn(processInstanceMockList);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }

        Q2cInsuranceArException ex = assertThrows(Q2cInsuranceArException.class, () -> {
            arInvoicesService.getProcessInstance("test.txt","121323454");
        });

        String expectedMessage = "No batch file found.";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetProcessFileDetail() {
        List<ProcessFileDetailsDTO> fileDetailsDTOListMock = new ArrayList<>();

        ProcessFileDetailsDTO dto = getProcessFileDetailDto();

        fileDetailsDTOListMock.add(dto);

        try {
            when(dbOracleServiceMock.getProcessFileDetailsDTOs(anyString())).thenReturn(fileDetailsDTOListMock);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }

        List<ProcessFileDetailsDTO> fileDetailList = new ArrayList<>();

        try {
            fileDetailList = arInvoicesService.getProcessFileDetail("test.txt");
        } catch (Q2cInsuranceArException e) {
            e.printStackTrace();
        }

        assertNotNull(fileDetailList);
        assertEquals(1, fileDetailList.size());

    }

    @Test
    void testIsProcessFileDetailsSuccess() {
        ProcessFileDetailsDTO dto = getProcessFileDetailDto();
        dto.setLoadStatus("COMPLETED");

        boolean isProcessFileDetail = arInvoicesService.isProcessFileDetailsSuccess(dto);

        assertTrue(isProcessFileDetail);
    }

    @Test
    void testIsProcessFileDetailsNotSuccess() {
        ProcessFileDetailsDTO dto = getProcessFileDetailDto();
        dto.setLoadStatus("INCOMPLETED");

        boolean isProcessFileDetail = arInvoicesService.isProcessFileDetailsSuccess(dto);

        assertFalse(isProcessFileDetail);
    }

    @Test
    void testIsProcessInstanceTrSuccess() {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto = getProcessInstanceDto();
        dto.setTransformationStatus("TRANSFORMED_ERP");
        dto.setStatus("SUCCESS");

        boolean isProcessInstance = arInvoicesService.isProcessInstanceTrSuccess(dto);

        assertTrue(isProcessInstance);
    }

    @Test
    void testIsProcessInstanceTrNotSuccess() {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto = getProcessInstanceDto();
        dto.setTransformationStatus("TEST");
        dto.setStatus("FAILED");

        boolean isProcessInstance = arInvoicesService.isProcessInstanceTrSuccess(dto);

        assertFalse(isProcessInstance);
    }

    @Test
    void testGetInterfaceLine() {
        InterfaceLinesDTO dto = getInterfaceLinesDTO();
        List<InterfaceLinesDTO> lists = new ArrayList<>();

        lists.add(dto);

        try {
            when(dbOracleServiceMock.getInterfaceLinesDTOs(anyList())).thenReturn(lists);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
        List<String> listString = new ArrayList<>();
        List<InterfaceLinesDTO> interfaceLinesDTOLines = new ArrayList<>();
        try {
            interfaceLinesDTOLines = arInvoicesService.getInterfaceLine(listString);
        } catch (Q2cInsuranceArException e) {
            e.printStackTrace();
        }
        assertNotNull(interfaceLinesDTOLines);
        assertEquals(1, interfaceLinesDTOLines.size());

    }

    @Test
    void testGetJobHistoryInLines() {

        JobHistoryDTO dto = getJobHistoryDTO();
        List<JobHistoryDTO> lists = new ArrayList<>();

        lists.add(dto);

        try {
            when(dbOracleServiceMock.getJobHistoryInLinesDTOs(anyList())).thenReturn(lists);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
        List<String> listString = new ArrayList<>();
        List<JobHistoryDTO> jobHistoryDTOs = new ArrayList<>();
        try {
            jobHistoryDTOs = arInvoicesService.getJobHistoryInLines(listString);
        } catch (Q2cInsuranceArException e) {
            e.printStackTrace();
        }
        assertNotNull(jobHistoryDTOs);
        assertEquals(1, jobHistoryDTOs.size());


    }

    @Test
    void testIsInterfaceLineSuccess() {
        InterfaceLinesDTO dto = getInterfaceLinesDTO();

        boolean isInterfaceLine = arInvoicesService.isInterfaceLineSuccess(dto);

        assertTrue(isInterfaceLine);
    }

    @Test
    void testIsInterfaceLineNotSuccess() {
        InterfaceLinesDTO dto = new InterfaceLinesDTO();
        dto.setStatus("NOT_VALIDATED");

        boolean isInterfaceLine = arInvoicesService.isInterfaceLineSuccess(dto);

        assertFalse(isInterfaceLine);
    }

    @Test
    void testIsJobHistoryInLineSuccess() {
        JobHistoryDTO dto = getJobHistoryDTO();

        boolean isJobHistory = arInvoicesService.isJobHistoryInLineSuccess(dto);

        assertTrue(isJobHistory);
    }

    @Test
    void testIsJobHistoryInLineNotSuccess() {
        JobHistoryDTO dto = new JobHistoryDTO();
        dto.setJobStatus("FAILED");

        boolean isJobHistory = arInvoicesService.isJobHistoryInLineSuccess(dto);

        assertFalse(isJobHistory);
    }

    private InterfaceLinesDTO getInterfaceLinesDTO() {
        InterfaceLinesDTO dto = new InterfaceLinesDTO();

        dto.setStatus("VALIDATED");
        dto.setInstanceId(4213432L);
        dto.setAlternateDocumentId("test.txt");

        return dto;

    }

    private JobHistoryDTO getJobHistoryDTO(){
        JobHistoryDTO dto = new JobHistoryDTO();

        dto.setJobStatus("SUCCESS");
        dto.setJobId("234324");
        
        return dto;
    }

    private ProcessInstanceDTO getProcessInstanceDto() {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();

        dto.setAicInstanceId("test");
        dto.setAttribute1("1");
        dto.setBatchName("batchfile.txt");

        return dto;
    }

    private ProcessFileDetailsDTO getProcessFileDetailDto() {
        ProcessFileDetailsDTO dto = new ProcessFileDetailsDTO();

        dto.setInstanceId(432432L);
        dto.setFileName("test.txt");

        return dto;
    }

}
