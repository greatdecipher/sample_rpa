package com.albertsons.argus.domaindbq2c.service.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.DomainDBQ2CApplication;
import com.albertsons.argus.domaindbq2c.dto.BankProcessInstanceDTO;
import com.albertsons.argus.domaindbq2c.dto.BankStatementsDTO;
import com.albertsons.argus.domaindbq2c.dto.InterfaceLinesDTO;
import com.albertsons.argus.domaindbq2c.dto.JobHistoryDTO;
import com.albertsons.argus.domaindbq2c.dto.LockboxHdrTrailPayDTO;
import com.albertsons.argus.domaindbq2c.dto.OutboundProcessDetailsDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessFileDetailsDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessFileDetailsFilteredDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbq2c.dto.TargetedProcessFileDetailsDTO;
import com.albertsons.argus.domaindbq2c.exception.OracleServiceException;
import com.albertsons.argus.domaindbq2c.service.DbOracleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DbOracleServiceImplTest.class)
class DbOracleServiceImplTest{
    
    @MockBean
    private DbOracleService mockOracleService;

    @BeforeEach
    void setBankProcessInstanceMockOutput() {
        List<BankProcessInstanceDTO> list = new ArrayList<>();

        BankProcessInstanceDTO dto = new BankProcessInstanceDTO();

        dto = new BankProcessInstanceDTO();
        dto.setInstanceId(000001L);
        dto.setBatchName("Test Batch Name");
        dto.setIntegrationName("Test Integration Name");
        dto.setIntegrationPattern("Test Integration Pattern");
        dto.setStatus("Test Status");
        dto.setTransformationStatus("Test Transformation Status");
        dto.setInterfaceId("Test Interface Id");
        dto.setRunDate("Test Run Date");
        dto.setAttribute1("Test Attribute 1");
        dto.setAttribute2("Test Attribute 2");
        dto.setProcessTime("Test Process Time");
        
        list.add(dto);

        try {
            when(mockOracleService.getBankProcessInstanceDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void setBankStatementsMockOutput() {
        List<BankStatementsDTO> list = new ArrayList<>();

        BankStatementsDTO dto = new BankStatementsDTO();

        dto = new BankStatementsDTO();
        dto.setInstanceId(000001L);
        dto.setCallbackId("Test");
        dto.setJobId("Test");
        dto.setJobName("Test");
        dto.setJobStatus("Test");
        dto.setProcessTime("Test");
        dto.setAlternateDocumentId("Test");
        dto.setFileName("Test");
        dto.setCount(10);
        
        list.add(dto);

        try {
            when(mockOracleService.getBankStatementsDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void setInterfaceLinesMockOutput() {
        List<InterfaceLinesDTO> list = new ArrayList<>();

        InterfaceLinesDTO dto = new InterfaceLinesDTO();

        dto = new InterfaceLinesDTO();
        dto.setInstanceId(000001L);
        dto.setAlternateDocumentId("Test");
        dto.setStatus("Test");
        dto.setErrorMessage("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getInterfaceLinesDTOs(anyList())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void setJobHistoryInLinesMockOutput() {
        List<JobHistoryDTO> list = new ArrayList<>();

        JobHistoryDTO dto = new JobHistoryDTO();
        dto.setAlternateDocumentId("Test");
        dto.setInstanceId(000001L);
        dto.setCallbackId("Test");
        dto.setJobId("Test");
        dto.setJobName("Test");
        dto.setJobStatus("Test");
        dto.setProcessTime("Test");

        list.add(dto);

        try {
            when(mockOracleService.getJobHistoryInLinesDTOs(anyList())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setJobHistoryLockboxMockOutput() {
        List<JobHistoryDTO> list = new ArrayList<>();

        JobHistoryDTO dto = new JobHistoryDTO();
        dto.setAlternateDocumentId("Test");
        dto.setInstanceId(000001L);
        dto.setCallbackId("Test");
        dto.setJobId("Test");
        dto.setJobName("Test");
        dto.setJobStatus("Test");
        dto.setProcessTime("Test");

        list.add(dto);

        try {
            when(mockOracleService.getJobHistoryLockboxDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setJobHistoryTrxsMockOutput() {
        List<JobHistoryDTO> list = new ArrayList<>();

        JobHistoryDTO dto = new JobHistoryDTO();
        dto.setAlternateDocumentId("Test");
        dto.setInstanceId(000001L);
        dto.setCallbackId("Test");
        dto.setJobId("Test");
        dto.setJobName("Test");
        dto.setJobStatus("Test");
        dto.setProcessTime("Test");

        list.add(dto);

        try {
            when(mockOracleService.getJobHistoryTrxsDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void setLockboxHdrTrailPayMockOutput() {
        List<LockboxHdrTrailPayDTO> list = new ArrayList<>();

        LockboxHdrTrailPayDTO dto = new LockboxHdrTrailPayDTO();
        dto.setRecordType("Test");
        dto.setBatchName("Test");
        dto.setCurrencyCode("Test");
        dto.setExchangeRateType("Test");
        dto.setExchangeRate("Test");
        dto.setReceiptMethod("Test");
        dto.setLockboxNumber("Test");
        dto.setDepositDate("Test");
        dto.setDepositTime("Test");
        dto.setBatchRecordCount("Test");
        dto.setBatchAmount("Test");
        dto.setComments("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        dto.setAttribute6("Test");
        dto.setAttribute7("Test");
        dto.setAttribute8("Test");
        dto.setAttribute9("Test");
        dto.setAttribute10("Test");
        dto.setAttribute11("Test");
        dto.setAttribute12("Test");
        dto.setAttribute13("Test");
        dto.setAttribute14("Test");
        dto.setAttribute15("Test");
        dto.setAttributeCategory("Test");
        dto.setItemNumber("Test");
        dto.setOverflowIndicator("Test");
        dto.setOverflowSequence("Test");
        dto.setRemittanceAmount("Test");
        dto.setTransitRoutingNumber("Test");
        dto.setAccount("Test");
        dto.setCheckNumber("Test");
        dto.setReceiptDate("Test");
        dto.setCustomerNumber("Test");
        dto.setBillToLocation("Test");
        dto.setCustomerBankBranchName("Test");
        dto.setCustomerBankName("Test");
        dto.setRemittanceBankBranchName("Test");
        dto.setRemittanceBankName("Test");
        dto.setAnticipatedClearingDate("Test");
        dto.setInvoice1("Test");
        dto.setInvoice1Installment("Test");
        dto.setMatching1Date("Test");
        dto.setInvoiceCurrencyCode1("Test");
        dto.setTransToReceiptRate1("Test");
        dto.setAmountApplied1("Test");
        dto.setAmountAppliedFrom1("Test");
        dto.setCustomerReference1("Test");
        dto.setInvoice2("Test");
        dto.setInvoice2Installment("Test");
        dto.setMatching2Date("Test");
        dto.setInvoiceCurrencyCode2("Test");
        dto.setTransToReceiptRate2("Test");
        dto.setAmountApplied2("Test");
        dto.setAmountAppliedFrom2("Test");
        dto.setCustomerReference2("Test");
        dto.setInvoice3("Test");
        dto.setInvoice3Installment("Test");
        dto.setMatching3Date("Test");
        dto.setInvoiceCurrencyCode3("Test");
        dto.setTransToReceiptRate3("Test");
        dto.setAmountApplied3("Test");
        dto.setAmountAppliedFrom3("Test");
        dto.setCustomerReference3("Test");
        dto.setInvoice4("Test");
        dto.setInvoice4Installment("Test");
        dto.setMatching4Date("Test");
        dto.setInvoiceCurrencyCode4("Test");
        dto.setTransToReceiptRate4("Test");
        dto.setAmountApplied4("Test");
        dto.setAmountAppliedFrom4("Test");
        dto.setCustomerReference4("Test");
        dto.setInvoice5("Test");
        dto.setInvoice5Installment("Test");
        dto.setMatching5Date("Test");
        dto.setInvoiceCurrencyCode5("Test");
        dto.setTransToReceiptRate5("Test");
        dto.setAmountApplied5("Test");
        dto.setAmountAppliedFrom5("Test");
        dto.setCustomerReference5("Test");
        dto.setInvoice6("Test");
        dto.setInvoice6Installment("Test");
        dto.setMatching6Date("Test");
        dto.setInvoiceCurrencyCode6("Test");
        dto.setTransToReceiptRate6("Test");
        dto.setAmountApplied6("Test");
        dto.setAmountAppliedFrom6("Test");
        dto.setCustomerReference6("Test");
        dto.setInvoice7("Test");
        dto.setInvoice7Installment("Test");
        dto.setMatching7Date("Test");
        dto.setInvoiceCurrencyCode7("Test");
        dto.setTransToReceiptRate7("Test");
        dto.setAmountApplied7("Test");
        dto.setAmountAppliedFrom7("Test");
        dto.setCustomerReference7("Test");
        dto.setInvoice8("Test");
        dto.setInvoice8Installment("Test");
        dto.setMatching8Date("Test");
        dto.setInvoiceCurrencyCode8("Test");
        dto.setTransToReceiptRate8("Test");
        dto.setAmountApplied8("Test");
        dto.setAmountAppliedFrom8("Test");
        dto.setCustomerReference8("Test");
        dto.setAttributeNumber1("Test");
        dto.setAttributeNumber2("Test");
        dto.setAttributeNumber3("Test");
        dto.setAttributeNumber4("Test");
        dto.setAttributeNumber5("Test");
        dto.setAttributeNumber6("Test");
        dto.setAttributeNumber7("Test");
        dto.setAttributeNumber8("Test");
        dto.setAttributeNumber9("Test");
        dto.setAttributeNumber10("Test");
        dto.setAttributeNumber11("Test");
        dto.setAttributeNumber12("Test");
        dto.setAttributeNumber13("Test");
        dto.setAttributeNumber14("Test");
        dto.setAttributeNumber15("Test");
        dto.setBatchNumber("Test");
        dto.setInstanceId(000001L);
        dto.setProcessTime("Test");
        dto.setScheduledInstanceId("Test");
        dto.setErrorMessage("Test");
        dto.setStatus("Test");
        dto.setFilename("Test");
        dto.setRecordCount("Test");
        dto.setFileCreationDate("Test");
        dto.setLastUpdateDate("Test");
        dto.setLastUpdatedBy("Test");
        dto.setLastUpdateLogin("Test");
        dto.setCreationDate("Test");
        dto.setCreatedBy("Test");
        dto.setObjectVersionNumber("Test");
        dto.setPartitionNumber("Test");
        dto.setOffset("Test");
        dto.setConsumerInstance("Test");
        dto.setTopic("Test");
        dto.setConsumerGroup("Test");
        dto.setIntegrationType("Test");
        dto.setFormatNumber("Test");
        dto.setPaymentDate("Test");
        dto.setSourceApplicationCode("Test");
        dto.setPaymentNumber("Test");
        dto.setPaymentDescription("Test");
        dto.setPaymentType("Test");
        dto.setPaymentCurrency("Test");
        dto.setBusinessUnit("Test");
        dto.setPayeeName("Test");
        dto.setPayeeSite("Test");
        dto.setVendorId("Test");
        dto.setVendorNum("Test");
        dto.setVendorSubAcctId("Test");
        dto.setBankAccountName("Test");
        dto.setBankAccountNumber("Test");
        dto.setPaymentMethod("Test");
        dto.setPaymentProcessProfile("Test");
        dto.setPaymentDocument("Test");
        dto.setInvoiceNum("Test");
        dto.setInstallmentNumber("Test");
        dto.setAmountPaidInvCurr("Test");
        dto.setDiscountTaken("Test");
        dto.setAccountingUnit("Test");
        dto.setParentInstanceId("Test");
        dto.setMessageSequenceNbr("Test");
        dto.setExpectedMessageCnt("Test");
        dto.setAlternateDocumentId("Test");  

        list.add(dto);

        try {
            when(mockOracleService.getLockboxHdrTrailPayDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void setProcessFileDetailsMockOutput() {
        List<ProcessFileDetailsDTO> list = new ArrayList<>();

        ProcessFileDetailsDTO dto = new ProcessFileDetailsDTO();
        dto.setInstanceId(70000000L);
        dto.setFileName("JEJIFSC20220101231111.dat");
        dto.setFileSize("1");
        dto.setInterfaceId("INT46");
        dto.setFileRowCount("Test");
        dto.setLoadRowCount("Test");
        dto.setLoadDateTime("Test");
        dto.setLoadStatus("TRANSFORMEDNOVAL");
        dto.setValidationFailureRowCount("Test");
        dto.setValidationStatus("Test");
        dto.setValidationDateTime("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        dto.setAttribute6("Test");
        dto.setAttribute7("Test");
        dto.setAttribute8("Test");
        dto.setAttribute9("Test");
        dto.setAttribute10("Test");
        dto.setAttribute11("Test");
        dto.setAttribute12("Test");
        dto.setAttribute13("Test");
        dto.setAttribute14("Test");
        dto.setAttribute15("Test");
        dto.setProcessTime("12-MAY-22 11.37.03.531805000 AM");

        list.add(dto);

        try {
            when(mockOracleService.getProcessFileDetailsDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setProcessFileDetailsExactMockOutput() {
        List<ProcessFileDetailsDTO> list = new ArrayList<>();

        ProcessFileDetailsDTO dto = new ProcessFileDetailsDTO();
        dto.setInstanceId(70000000L);
        dto.setFileName("JEJIFSC20220101231111.dat");
        dto.setFileSize("1");
        dto.setInterfaceId("INT46");
        dto.setFileRowCount("Test");
        dto.setLoadRowCount("Test");
        dto.setLoadDateTime("Test");
        dto.setLoadStatus("TRANSFORMEDNOVAL");
        dto.setValidationFailureRowCount("Test");
        dto.setValidationStatus("Test");
        dto.setValidationDateTime("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        dto.setAttribute6("Test");
        dto.setAttribute7("Test");
        dto.setAttribute8("Test");
        dto.setAttribute9("Test");
        dto.setAttribute10("Test");
        dto.setAttribute11("Test");
        dto.setAttribute12("Test");
        dto.setAttribute13("Test");
        dto.setAttribute14("Test");
        dto.setAttribute15("Test");
        dto.setProcessTime("12-MAY-22 11.37.03.531805000 AM");

        list.add(dto);

        try {
            when(mockOracleService.getProcessFileDetailsExactDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void setProcessFileDetailsFilteredMockOutput() {
        List<ProcessFileDetailsFilteredDTO> list = new ArrayList<>();

        ProcessFileDetailsFilteredDTO dto = new ProcessFileDetailsFilteredDTO();
        dto.setFileName("JEJIFSC20220101231111.dat");
        dto.setAttribute2("Test");
        dto.setAttribute4("Test");
        dto.setLoadStatus("TRANSFORMEDNOVAL");
        dto.setProcessTime("12-MAY-22 11.37.03.531805000 AM");

        list.add(dto);

        try {
            when(mockOracleService.getProcessFileDetailsFilteredDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setTargetedProcessFileDetailsMockOutput() {
        List<TargetedProcessFileDetailsDTO> list = new ArrayList<>();

        TargetedProcessFileDetailsDTO dto = new TargetedProcessFileDetailsDTO();

        dto.setFileName("REM_LCKBXL1");
        dto.setLoadStatus("Test");
        dto.setLoadDate("Test");
        dto.setExpectedMessageCnt(400);
        dto.setConsumedRec(25);
        dto.setStartProcess("Test");
        dto.setEndProcess("Test");
        dto.setBoxName("Test");
        dto.setSourceApplicationCode("Test");
        dto.setBatchName("Test");
        dto.setRemittanceAmount("Test");
        dto.setInstanceId(1111111L);
        dto.setProcessTime("12-MAY-22 11.37.03.531805000 AM");
        dto.setStatus("PROCESSED");
        dto.setTransformationStatus("TRANSFORMED_ERP");
        dto.setTransformationMsg("Test");

        list.add(dto);

        try {
            when(mockOracleService.getTargetedProcessFileDetailsDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setProcessInstanceByDescMockOutput(){
        List<ProcessInstanceDTO> list = new ArrayList<>();

        ProcessInstanceDTO piDTO = new ProcessInstanceDTO();
		piDTO = new ProcessInstanceDTO();
		piDTO.setInstanceId(90000000L);
		piDTO.setIntegrationName("Journal Entries to Oracle Cloud GL");
		piDTO.setIntegrationPattern("Inbound");
		piDTO.setRunDate("Test");
		piDTO.setStartTime("Test");
		piDTO.setEndTime("Test");
		piDTO.setStatusTime("Test");
		piDTO.setAttribute1("Test");
		piDTO.setAttribute2("Test");
		piDTO.setAttribute3("Test");
		piDTO.setAttribute4("Test");
		piDTO.setAttribute5("Test");
		piDTO.setAttribute6("Test");
		piDTO.setAttribute7("Test");
		piDTO.setAttribute8("Test");
		piDTO.setAttribute9("Test");
		piDTO.setAttribute10("Test");
		piDTO.setAttribute11("Test");
		piDTO.setAttribute12("Test");
		piDTO.setAttribute13("Test");
		piDTO.setAttribute14("Test");
		piDTO.setAttribute15("Test");
		piDTO.setStatus("Test");
		piDTO.setAicInstanceId("Test");
		piDTO.setTransformationStatus("TRANSFORMEDERP");
		piDTO.setStartBatch("Test");
		piDTO.setEndBatch("Test");
		piDTO.setInterfaceId("INT46");
		piDTO.setTransformationMsg("Test");
		piDTO.setBatchName("JEJIFSC20220101000000.dat");
		piDTO.setParentInstanceId("Test");
		piDTO.setProcessTime("Test");

        list.add(piDTO);

        try {
            when(mockOracleService.getProcessInstanceDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setProcessInstanceMockOutput(){
        List<ProcessInstanceDTO> list = new ArrayList<>();

        ProcessInstanceDTO piDTO = new ProcessInstanceDTO();
		piDTO = new ProcessInstanceDTO();
		piDTO.setInstanceId(90000000L);
		piDTO.setIntegrationName("Journal Entries to Oracle Cloud GL");
		piDTO.setIntegrationPattern("Inbound");
		piDTO.setRunDate("Test");
		piDTO.setStartTime("Test");
		piDTO.setEndTime("Test");
		piDTO.setStatusTime("Test");
		piDTO.setAttribute1("Test");
		piDTO.setAttribute2("Test");
		piDTO.setAttribute3("Test");
		piDTO.setAttribute4("Test");
		piDTO.setAttribute5("Test");
		piDTO.setAttribute6("Test");
		piDTO.setAttribute7("Test");
		piDTO.setAttribute8("Test");
		piDTO.setAttribute9("Test");
		piDTO.setAttribute10("Test");
		piDTO.setAttribute11("Test");
		piDTO.setAttribute12("Test");
		piDTO.setAttribute13("Test");
		piDTO.setAttribute14("Test");
		piDTO.setAttribute15("Test");
		piDTO.setStatus("Test");
		piDTO.setAicInstanceId("Test");
		piDTO.setTransformationStatus("TRANSFORMEDERP");
		piDTO.setStartBatch("Test");
		piDTO.setEndBatch("Test");
		piDTO.setInterfaceId("INT46");
		piDTO.setTransformationMsg("Test");
		piDTO.setBatchName("JEJIFSC20220101000000.dat");
		piDTO.setParentInstanceId("Test");
		piDTO.setProcessTime("Test");

        list.add(piDTO);

        try {
            when(mockOracleService.getProcessInstanceDTOs(anyString())).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void setArCustProcessDetailsMockOutput() {
        List<OutboundProcessDetailsDTO> list = new ArrayList<>();

        OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

        dto = new OutboundProcessDetailsDTO();
        dto.setInstanceId(000001L);
        dto.setIntegrationId("Test");
        dto.setStatus("Test");
        dto.setProcessTime("Test");
        dto.setStartDate("Test");
        dto.setEndDate("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getArCustProcessDetailsDTOs()).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setCrosswalkProcessDetailsMockOutput() {
        List<OutboundProcessDetailsDTO> list = new ArrayList<>();

        OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

        dto = new OutboundProcessDetailsDTO();
        dto.setInstanceId(000001L);
        dto.setIntegrationId("Test");
        dto.setStatus("Test");
        dto.setProcessTime("Test");
        dto.setStartDate("Test");
        dto.setEndDate("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getCrosswalkProcessDetailsDTOs()).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setCustBalProcessDetailsMockOutput() {
        List<OutboundProcessDetailsDTO> list = new ArrayList<>();

        OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

        dto = new OutboundProcessDetailsDTO();
        dto.setInstanceId(000001L);
        dto.setIntegrationId("Test");
        dto.setStatus("Test");
        dto.setProcessTime("Test");
        dto.setStartDate("Test");
        dto.setEndDate("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getCustBalProcessDetailsDTOs()).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setCustStatementsProcessDetailsMockOutput() {
        List<OutboundProcessDetailsDTO> list = new ArrayList<>();

        OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

        dto = new OutboundProcessDetailsDTO();
        dto.setInstanceId(000001L);
        dto.setIntegrationId("Test");
        dto.setStatus("Test");
        dto.setProcessTime("Test");
        dto.setStartDate("Test");
        dto.setEndDate("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getCustStatementsProcessDetailsDTOs()).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setPosPayAndVoidsProcessDetailsMockOutput() {
        List<OutboundProcessDetailsDTO> list = new ArrayList<>();

        OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

        dto = new OutboundProcessDetailsDTO();
        dto.setInstanceId(000001L);
        dto.setIntegrationId("Test");
        dto.setStatus("Test");
        dto.setProcessTime("Test");
        dto.setStartDate("Test");
        dto.setEndDate("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getPosPayAndVoidsProcessDetailsDTOs()).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setPrgExtractsProcessDetailsMockOutput() {
        List<OutboundProcessDetailsDTO> list = new ArrayList<>();

        OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

        dto = new OutboundProcessDetailsDTO();
        dto.setInstanceId(000001L);
        dto.setIntegrationId("Test");
        dto.setStatus("Test");
        dto.setProcessTime("Test");
        dto.setStartDate("Test");
        dto.setEndDate("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getPrgExtractsProcessDetailsDTOs()).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setSDChecksProcessDetailsMockOutput() {
        List<OutboundProcessDetailsDTO> list = new ArrayList<>();

        OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

        dto = new OutboundProcessDetailsDTO();
        dto.setInstanceId(000001L);
        dto.setIntegrationId("Test");
        dto.setStatus("Test");
        dto.setProcessTime("Test");
        dto.setStartDate("Test");
        dto.setEndDate("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getSDChecksProcessDetailsDTOs()).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUwareProcessDetailsMockOutput() {
        List<OutboundProcessDetailsDTO> list = new ArrayList<>();

        OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

        dto = new OutboundProcessDetailsDTO();
        dto.setInstanceId(000001L);
        dto.setIntegrationId("Test");
        dto.setStatus("Test");
        dto.setProcessTime("Test");
        dto.setStartDate("Test");
        dto.setEndDate("Test");
        dto.setAttribute1("Test");
        dto.setAttribute2("Test");
        dto.setAttribute3("Test");
        dto.setAttribute4("Test");
        dto.setAttribute5("Test");
        
        list.add(dto);

        try {
            when(mockOracleService.getUwareProcessDetailsDTOs()).thenReturn(list);
        } catch (OracleServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetBankProcessInstanceDTOs() throws OracleServiceException {
        List<BankProcessInstanceDTO> dto = mockOracleService.getBankProcessInstanceDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetBankStatementsDTOs() throws OracleServiceException {
        List<BankStatementsDTO> dto = mockOracleService.getBankStatementsDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetInterfaceLinesDTOs() throws OracleServiceException {
        List<String> filenames = new ArrayList<>();
        filenames.add("JEJIFSC20220101231111.dat");
        List<InterfaceLinesDTO> dto = mockOracleService.getInterfaceLinesDTOs(filenames);
        assertNotNull(dto);
    }

    @Test
    void testGetJobHistoryInLinesDTOs() throws OracleServiceException {
        List<String> filenames = new ArrayList<>();
        filenames.add("JEJIFSC20220101231111.dat");
        List<JobHistoryDTO> dto = mockOracleService.getJobHistoryInLinesDTOs(filenames);
        assertNotNull(dto);
    }

    @Test
    void testGetJobHistoryLockboxDTOs() throws OracleServiceException {
        List<JobHistoryDTO> dto = mockOracleService.getJobHistoryLockboxDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetJobHistoryTrxsDTOs() throws OracleServiceException {
        List<JobHistoryDTO> dto = mockOracleService.getJobHistoryTrxsDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetLockboxHdrTrailPayDTOs() throws OracleServiceException {
        List<LockboxHdrTrailPayDTO> dto = mockOracleService.getLockboxHdrTrailPayDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetTargetedProcessFileDetailsDTOs() throws OracleServiceException {
        List<TargetedProcessFileDetailsDTO> dto = mockOracleService.getTargetedProcessFileDetailsDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetProcessFileDetailsDTOs() throws OracleServiceException {
        List<ProcessFileDetailsDTO> dto = mockOracleService.getProcessFileDetailsDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetProcessFileDetailsExactDTOs() throws OracleServiceException {
        List<ProcessFileDetailsDTO> dto = mockOracleService.getProcessFileDetailsExactDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetProcessFileDetailsFilteredDTOs() throws OracleServiceException {
        List<ProcessFileDetailsFilteredDTO> dto = mockOracleService.getProcessFileDetailsFilteredDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetProcessInstanceByDescDTOs() throws OracleServiceException {
        List<ProcessInstanceDTO> dto = mockOracleService.getProcessInstanceByDescDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetProcessInstanceDTOs() throws OracleServiceException {
        List<ProcessInstanceDTO> dto = mockOracleService.getProcessInstanceDTOs("JEJIFSC20220101231111.dat");
        assertNotNull(dto);
    }

    @Test
    void testGetArCustProcessDetailsDTOs() throws OracleServiceException {
        List<OutboundProcessDetailsDTO> dto = mockOracleService.getArCustProcessDetailsDTOs();
        assertNotNull(dto);
    }

    @Test
    void testGetCrossWalkProcessDetailsDTOs() throws OracleServiceException {
        List<OutboundProcessDetailsDTO> dto = mockOracleService.getCrosswalkProcessDetailsDTOs();
        assertNotNull(dto);
    }

    @Test
    void testGetCustBalProcessDetailsDTOs() throws OracleServiceException {
        List<OutboundProcessDetailsDTO> dto = mockOracleService.getCustBalProcessDetailsDTOs();
        assertNotNull(dto);
    }

    @Test
    void testGetCustStatementsProcessDetailsDTOs() throws OracleServiceException {
        List<OutboundProcessDetailsDTO> dto = mockOracleService.getCustStatementsProcessDetailsDTOs();
        assertNotNull(dto);
    }

    @Test
    void testGetPosPayAndVoidsProcessDetailsDTOs() throws OracleServiceException {
        List<OutboundProcessDetailsDTO> dto = mockOracleService.getPosPayAndVoidsProcessDetailsDTOs();
        assertNotNull(dto);
    }

    @Test
    void testGetPrgExtractsProcessDetailsDTOs() throws OracleServiceException {
        List<OutboundProcessDetailsDTO> dto = mockOracleService.getPrgExtractsProcessDetailsDTOs();
        assertNotNull(dto);
    }

    @Test
    void testGetSDChecksProcessDetailsDTOs() throws OracleServiceException {
        List<OutboundProcessDetailsDTO> dto = mockOracleService.getSDChecksProcessDetailsDTOs();
        assertNotNull(dto);
    }

    @Test
    void testGetUwareProcessDetailsDTOs() throws OracleServiceException {
        List<OutboundProcessDetailsDTO> dto = mockOracleService.getUwareProcessDetailsDTOs();
        assertNotNull(dto);
    }
    
}

    