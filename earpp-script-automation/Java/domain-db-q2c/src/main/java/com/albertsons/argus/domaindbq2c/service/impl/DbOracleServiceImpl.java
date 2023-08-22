package com.albertsons.argus.domaindbq2c.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albertsons.argus.dataq2c.bo.oracle.custom.BankProcessInstanceBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.BankStatementsBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.InterfaceLinesBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.JobHistoryBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.LockboxHdrTrailPayBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.OutboundProcessDetailsBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.ProcessFileDetailsBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.ProcessFileDetailsFilteredBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.ProcessInstanceBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.TargetedProcessFileDetailsBO;
import com.albertsons.argus.dataq2c.repo.oracle.GetOracleTablesCustomRepo;
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

@Service
public class DbOracleServiceImpl implements DbOracleService {
    @Autowired
    private GetOracleTablesCustomRepo repo;

    @Override
    public List<BankProcessInstanceDTO> getBankProcessInstanceDTOs(String filename) throws OracleServiceException {
        try {
            List<BankProcessInstanceBO> bos = new ArrayList<>();
            List<BankProcessInstanceDTO> dtoList = new ArrayList<>();
            
            bos = repo.getBankProcessInstanceBOs(filename);

            BankProcessInstanceDTO dto = new BankProcessInstanceDTO();

            for(BankProcessInstanceBO bo : bos){
                dto = new BankProcessInstanceDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setBatchName(bo.getBatch_Name());
                dto.setIntegrationName(bo.getIntegration_Pattern());
                dto.setIntegrationPattern(bo.getIntegration_Name());
                dto.setStatus(bo.getStatus());
                dto.setTransformationStatus(bo.getTransformation_Status());
                dto.setInterfaceId(bo.getInterface_Id());
                dto.setRunDate(bo.getRun_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setProcessTime(bo.getProcess_Time());
                
                dtoList.add(dto);
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Bank Process Instance BOs failed.", e);
        }
    }

    @Override
    public List<BankStatementsDTO> getBankStatementsDTOs(String filename) throws OracleServiceException {
        try {
            List<BankStatementsBO> bos = new ArrayList<>();
            List<BankStatementsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getBankStatementsBOs(filename);

            BankStatementsDTO dto = new BankStatementsDTO();

            for(BankStatementsBO bo : bos){
                dto = new BankStatementsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setCallbackId(bo.getCallback_Id());
                dto.setJobId(bo.getJob_Id());
                dto.setJobName(bo.getJob_Name());
                dto.setJobStatus(bo.getJob_Status());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setAlternateDocumentId(bo.getAlternateDocumentId());
                dto.setFileName(bo.getFile_Name());
                dto.setCount(bo.getCount());
                
                dtoList.add(dto);
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Bank Statements BOs failed.", e);
        }
    }

    @Override
    public List<InterfaceLinesDTO> getInterfaceLinesDTOs(List<String> filenames) throws OracleServiceException {
        try {
            List<InterfaceLinesBO> bos = new ArrayList<>();
            List<InterfaceLinesDTO> dtoList = new ArrayList<>();
            
            bos = repo.getInterfaceLinesBOs(filenames);

            InterfaceLinesDTO dto = new InterfaceLinesDTO();

            for(InterfaceLinesBO bo : bos){
                dto = new InterfaceLinesDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setAlternateDocumentId(bo.getAlternateDocumentId());
                dto.setStatus(bo.getStatus());
                dto.setErrorMessage(bo.getError_Message());
                
                dtoList.add(dto);
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Interface Lines BOs failed.", e);
        }
    }

    @Override
    public List<InterfaceLinesDTO> getInterfaceLineCountDTOs(String filename) throws OracleServiceException {
        try {
            List<InterfaceLinesBO> bos = new ArrayList<>();
            List<InterfaceLinesDTO> dtoList = new ArrayList<>();
            
            bos = repo.getInterfaceLineCountBos(filename);

            InterfaceLinesDTO dto = new InterfaceLinesDTO();

            for(InterfaceLinesBO bo : bos){
                dto = new InterfaceLinesDTO();

                dto.setAlternateDocumentId(bo.getAlternateDocumentId());
                dto.setCount(bo.getCount());
                
                dtoList.add(dto);

                break;
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Interface Lines BOs failed.", e);
        }
    }

    

    @Override
    public List<JobHistoryDTO> getJobHistoryInLinesDTOs(List<String> filenames) throws OracleServiceException {
        try {
            List<JobHistoryBO> bos = new ArrayList<>();
            List<JobHistoryDTO> dtoList = new ArrayList<>();
            
            bos = repo.getJobHistoryInLinesBOs(filenames);

            JobHistoryDTO dto = new JobHistoryDTO();

            for(JobHistoryBO bo : bos){
                dto = new JobHistoryDTO();
                dto.setAlternateDocumentId(bo.getAlternateDocumentId());
                dto.setInstanceId(bo.getInstance_Id());
                dto.setCallbackId(bo.getCallback_Id());
                dto.setJobId(bo.getJob_Id());
                dto.setJobName(bo.getJob_Name());
                dto.setJobStatus(bo.getJob_Status());
                dto.setProcessTime(bo.getProcess_Time());
                
                dtoList.add(dto);
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Job History InLines BOs failed.", e);
        }
    }

    @Override
    public List<JobHistoryDTO> getJobHistoryLockboxDTOs(String filename) throws OracleServiceException {
        try {
            List<JobHistoryBO> bos = new ArrayList<>();
            List<JobHistoryDTO> dtoList = new ArrayList<>();
            
            bos = repo.getJobHistoryLockboxBOs(filename);

            JobHistoryDTO dto = new JobHistoryDTO();

            for(JobHistoryBO bo : bos){
                dto = new JobHistoryDTO();
                dto.setAlternateDocumentId(bo.getAlternateDocumentId());
                dto.setInstanceId(bo.getInstance_Id());
                dto.setCallbackId(bo.getCallback_Id());
                dto.setJobId(bo.getJob_Id());
                dto.setJobName(bo.getJob_Name());
                dto.setJobStatus(bo.getJob_Status());
                dto.setProcessTime(bo.getProcess_Time());
                
                dtoList.add(dto);
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Job History Locbox BOs failed.", e);
        }
    }

    @Override
    public List<TargetedProcessFileDetailsDTO> getTargetedProcessFileDetailsDTOs(String filename) throws OracleServiceException {
        try {
            List<TargetedProcessFileDetailsBO> bos = new ArrayList<>();
            List<TargetedProcessFileDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getTargetedProcessFileDetailsBOs(filename);

            TargetedProcessFileDetailsDTO dto = new TargetedProcessFileDetailsDTO();

            for(TargetedProcessFileDetailsBO bo : bos){
                dto = new TargetedProcessFileDetailsDTO();
                dto.setFileName(bo.getFile_Name());
                dto.setLoadStatus(bo.getLoad_Status());
                dto.setLoadDate(bo.getLoad_Date());
                dto.setExpectedMessageCnt(bo.getExpectedMessageCnt());
                dto.setConsumedRec(bo.getConsumed_Rec());
                dto.setStartProcess(bo.getStart_Process());
                dto.setEndProcess(bo.getEnd_Process());
                dto.setBoxName(bo.getBox_Name());
                dto.setSourceApplicationCode(bo.getSource_Application_Code());
                dto.setBatchName(bo.getBatch_Name());
                dto.setRemittanceAmount(bo.getRemittance_Amount());
                dto.setInstanceId(bo.getInstance_Id());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStatus(bo.getStatus());
                dto.setTransformationStatus(bo.getTransformation_Status());
                dto.setTransformationMsg(bo.getTransformation_Msg());
                
                dtoList.add(dto);
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Targeted Process File Details BOs failed.", e);
        }
    }

    @Override
    public List<JobHistoryDTO> getJobHistoryTrxsDTOs(String filename) throws OracleServiceException {
        try {
            List<JobHistoryBO> bos = new ArrayList<>();
            List<JobHistoryDTO> dtoList = new ArrayList<>();
            
            bos = repo.getJobHistoryTrxsBOs(filename);

            JobHistoryDTO dto = new JobHistoryDTO();

            for(JobHistoryBO bo : bos){
                dto = new JobHistoryDTO();
                dto.setAlternateDocumentId(bo.getAlternateDocumentId());
                dto.setInstanceId(bo.getInstance_Id());
                dto.setCallbackId(bo.getCallback_Id());
                dto.setJobId(bo.getJob_Id());
                dto.setJobName(bo.getJob_Name());
                dto.setJobStatus(bo.getJob_Status());
                dto.setProcessTime(bo.getProcess_Time());
                
                dtoList.add(dto);
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Job History Trxs BOs failed.", e);
        }
    }

    @Override
    public List<LockboxHdrTrailPayDTO> getLockboxHdrTrailPayDTOs(String filename) throws OracleServiceException {
        try {
            List<LockboxHdrTrailPayBO> bos = new ArrayList<>();
            List<LockboxHdrTrailPayDTO> dtoList = new ArrayList<>();
            
            bos = repo.getLockboxHdrTrailPayBOs(filename);

            LockboxHdrTrailPayDTO dto = new LockboxHdrTrailPayDTO();

            for(LockboxHdrTrailPayBO bo : bos){
                dto = new LockboxHdrTrailPayDTO();
                dto.setRecordType(bo.getRecord_Type());
                dto.setBatchName(bo.getBatch_Name());
                dto.setCurrencyCode(bo.getCurrency_Code());
                dto.setExchangeRateType(bo.getExchange_Rate_Type());
                dto.setExchangeRate(bo.getExchange_Rate());
                dto.setReceiptMethod(bo.getReceipt_Method());
                dto.setLockboxNumber(bo.getLockbox_Number());
                dto.setDepositDate(bo.getDeposit_Date());
                dto.setDepositTime(bo.getDeposit_Time());
                dto.setBatchRecordCount(bo.getBatch_Record_Count());
                dto.setBatchAmount(bo.getBatch_Amount());
                dto.setComments(bo.getComments());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());
                dto.setAttribute6(bo.getAttribute6());
                dto.setAttribute7(bo.getAttribute7());
                dto.setAttribute8(bo.getAttribute8());
                dto.setAttribute9(bo.getAttribute9());
                dto.setAttribute10(bo.getAttribute10());
                dto.setAttribute11(bo.getAttribute11());
                dto.setAttribute12(bo.getAttribute12());
                dto.setAttribute13(bo.getAttribute13());
                dto.setAttribute14(bo.getAttribute14());
                dto.setAttribute15(bo.getAttribute15());
                dto.setAttributeCategory(bo.getAttribute_Category());
                dto.setItemNumber(bo.getItem_Number());
                dto.setOverflowIndicator(bo.getOverflow_Indicator());
                dto.setOverflowSequence(bo.getOverflow_Sequence());
                dto.setRemittanceAmount(bo.getRemittance_Amount());
                dto.setTransitRoutingNumber(bo.getTransit_Routing_Number());
                dto.setAccount(bo.getAccount());
                dto.setCheckNumber(bo.getCheck_Number());
                dto.setReceiptDate(bo.getReceipt_Date());
                dto.setCustomerNumber(bo.getCustomer_Number());
                dto.setBillToLocation(bo.getBill_To_Location());
                dto.setCustomerBankBranchName(bo.getCustomer_Bank_Branch_Name());
                dto.setCustomerBankName(bo.getCustomer_Bank_Name());
                dto.setRemittanceBankBranchName(bo.getRemittance_Bank_Branch_Name());
                dto.setRemittanceBankName(bo.getRemittance_Bank_Name());
                dto.setAnticipatedClearingDate(bo.getAnticipated_Clearing_Date());
                dto.setInvoice1(bo.getInvoice1());
                dto.setInvoice1Installment(bo.getInvoice1_Installment());
                dto.setMatching1Date(bo.getMatching1_Date());
                dto.setInvoiceCurrencyCode1(bo.getInvoice_Currency_Code1());
                dto.setTransToReceiptRate1(bo.getTrans_To_Receipt_Rate1());
                dto.setAmountApplied1(bo.getAmount_Applied1());
                dto.setAmountAppliedFrom1(bo.getAmount_Applied_From1());
                dto.setCustomerReference1(bo.getCustomer_Reference1());
                dto.setInvoice2(bo.getInvoice2());
                dto.setInvoice2Installment(bo.getInvoice2_Installment());
                dto.setMatching2Date(bo.getMatching2_Date());
                dto.setInvoiceCurrencyCode2(bo.getInvoice_Currency_Code2());
                dto.setTransToReceiptRate2(bo.getTrans_To_Receipt_Rate2());
                dto.setAmountApplied2(bo.getAmount_Applied2());
                dto.setAmountAppliedFrom2(bo.getAmount_Applied_From2());
                dto.setCustomerReference2(bo.getCustomer_Reference2());
                dto.setInvoice3(bo.getInvoice3());
                dto.setInvoice3Installment(bo.getInvoice3_Installment());
                dto.setMatching3Date(bo.getMatching3_Date());
                dto.setInvoiceCurrencyCode3(bo.getInvoice_Currency_Code3());
                dto.setTransToReceiptRate3(bo.getTrans_To_Receipt_Rate3());
                dto.setAmountApplied3(bo.getAmount_Applied3());
                dto.setAmountAppliedFrom3(bo.getAmount_Applied_From3());
                dto.setCustomerReference3(bo.getCustomer_Reference3());
                dto.setInvoice4(bo.getInvoice4());
                dto.setInvoice4Installment(bo.getInvoice4_Installment());
                dto.setMatching4Date(bo.getMatching4_Date());
                dto.setInvoiceCurrencyCode4(bo.getInvoice_Currency_Code4());
                dto.setTransToReceiptRate4(bo.getTrans_To_Receipt_Rate4());
                dto.setAmountApplied4(bo.getAmount_Applied4());
                dto.setAmountAppliedFrom4(bo.getAmount_Applied_From4());
                dto.setCustomerReference4(bo.getCustomer_Reference4());
                dto.setInvoice5(bo.getInvoice5());
                dto.setInvoice5Installment(bo.getInvoice5_Installment());
                dto.setMatching5Date(bo.getMatching5_Date());
                dto.setInvoiceCurrencyCode5(bo.getInvoice_Currency_Code5());
                dto.setTransToReceiptRate5(bo.getTrans_To_Receipt_Rate5());
                dto.setAmountApplied5(bo.getAmount_Applied5());
                dto.setAmountAppliedFrom5(bo.getAmount_Applied_From5());
                dto.setCustomerReference5(bo.getCustomer_Reference5());
                dto.setInvoice6(bo.getInvoice6());
                dto.setInvoice6Installment(bo.getInvoice6_Installment());
                dto.setMatching6Date(bo.getMatching6_Date());
                dto.setInvoiceCurrencyCode6(bo.getInvoice_Currency_Code6());
                dto.setTransToReceiptRate6(bo.getTrans_To_Receipt_Rate6());
                dto.setAmountApplied6(bo.getAmount_Applied6());
                dto.setAmountAppliedFrom6(bo.getAmount_Applied_From6());
                dto.setCustomerReference6(bo.getCustomer_Reference6());
                dto.setInvoice7(bo.getInvoice7());
                dto.setInvoice7Installment(bo.getInvoice7_Installment());
                dto.setMatching7Date(bo.getMatching7_Date());
                dto.setInvoiceCurrencyCode7(bo.getInvoice_Currency_Code7());
                dto.setTransToReceiptRate7(bo.getTrans_To_Receipt_Rate7());
                dto.setAmountApplied7(bo.getAmount_Applied7());
                dto.setAmountAppliedFrom7(bo.getAmount_Applied_From7());
                dto.setCustomerReference7(bo.getCustomer_Reference7());
                dto.setInvoice8(bo.getInvoice8());
                dto.setInvoice8Installment(bo.getInvoice8_Installment());
                dto.setMatching8Date(bo.getMatching8_Date());
                dto.setInvoiceCurrencyCode8(bo.getInvoice_Currency_Code8());
                dto.setTransToReceiptRate8(bo.getAmount_Applied_From8());
                dto.setAmountApplied8(bo.getAmount_Applied8());
                dto.setAmountAppliedFrom8(bo.getAmount_Applied_From8());
                dto.setCustomerReference8(bo.getCustomer_Reference8());
                dto.setAttributeNumber1(bo.getAttribute_Number1());
                dto.setAttributeNumber2(bo.getAttribute_Number2());
                dto.setAttributeNumber3(bo.getAttribute_Number3());
                dto.setAttributeNumber4(bo.getAttribute_Number4());
                dto.setAttributeNumber5(bo.getAttribute_Number5());
                dto.setAttributeNumber6(bo.getAttribute_Number6());
                dto.setAttributeNumber7(bo.getAttribute_Number7());
                dto.setAttributeNumber8(bo.getAttribute_Number8());
                dto.setAttributeNumber9(bo.getAttribute_Number9());
                dto.setAttributeNumber10(bo.getAttribute_Number10());
                dto.setAttributeNumber11(bo.getAttribute_Number11());
                dto.setAttributeNumber12(bo.getAttribute_Number12());
                dto.setAttributeNumber13(bo.getAttribute_Number13());
                dto.setAttributeNumber14(bo.getAttribute_Number14());
                dto.setAttributeNumber15(bo.getAttribute_Number15());
                dto.setBatchNumber(bo.getBatch_Number());
                dto.setInstanceId(bo.getInstance_Id());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setScheduledInstanceId(bo.getScheduled_Instance_Id());
                dto.setErrorMessage(bo.getError_Message());
                dto.setStatus(bo.getStatus());
                dto.setFilename(bo.getFilename());
                dto.setRecordCount(bo.getRecord_Count());
                dto.setFileCreationDate(bo.getFile_Creation_Date());
                dto.setLastUpdateDate(bo.getLast_Update_Date());
                dto.setLastUpdatedBy(bo.getLast_Updated_By());
                dto.setLastUpdateLogin(bo.getLast_Update_Login());
                dto.setCreationDate(bo.getCreation_Date());
                dto.setCreatedBy(bo.getCreated_By());
                dto.setObjectVersionNumber(bo.getObject_Version_Number());
                dto.setPartitionNumber(bo.getPartition_Number());
                dto.setOffset(bo.getOffset());
                dto.setConsumerInstance(bo.getConsumer_Instance());
                dto.setTopic(bo.getTopic());
                dto.setConsumerGroup(bo.getConsumer_Group());
                dto.setIntegrationType(bo.getIntegration_Type());
                dto.setFormatNumber(bo.getFormat_Number());
                dto.setPaymentDate(bo.getPayment_Date());
                dto.setSourceApplicationCode(bo.getSource_Application_Code());
                dto.setPaymentNumber(bo.getPayment_Number());
                dto.setPaymentDescription(bo.getPayment_Description());
                dto.setPaymentType(bo.getPayment_Type());
                dto.setPaymentCurrency(bo.getPayment_Currency());
                dto.setBusinessUnit(bo.getBusiness_Unit());
                dto.setPayeeName(bo.getPayee_Name());
                dto.setPayeeSite(bo.getPayee_Site());
                dto.setVendorId(bo.getVendor_Id());
                dto.setVendorNum(bo.getVendor_Num());
                dto.setVendorSubAcctId(bo.getVendor_Sub_Acct_Id());
                dto.setBankAccountName(bo.getBank_Account_Name());
                dto.setBankAccountNumber(bo.getBank_Account_Number());
                dto.setPaymentMethod(bo.getPayment_Method());
                dto.setPaymentProcessProfile(bo.getPayment_Process_Profile());
                dto.setPaymentDocument(bo.getPayment_Document());
                dto.setInvoiceNum(bo.getInvoice_Num());
                dto.setInstallmentNumber(bo.getInstallment_Number());
                dto.setAmountPaidInvCurr(bo.getAmount_Paid_Inv_Curr());
                dto.setDiscountTaken(bo.getDiscount_Taken());
                dto.setAccountingUnit(bo.getAccounting_Unit());
                dto.setParentInstanceId(bo.getParentInstanceId());
                dto.setMessageSequenceNbr(bo.getMessageSequenceNbr());
                dto.setExpectedMessageCnt(bo.getExpectedMessageCnt());
                dto.setAlternateDocumentId(bo.getAlternateDocumentId());  
                
                dtoList.add(dto);
            }
            
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Job History Lockbox Hdr Trail Pay BOs failed.", e);
        }
    }

    @Override
    public List<ProcessFileDetailsDTO> getProcessFileDetailsDTOs(String filename) throws OracleServiceException {
        try {
            List<ProcessFileDetailsBO> bos = new ArrayList<>();
            List<ProcessFileDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getProcessFileDetailsBOs(filename);

            ProcessFileDetailsDTO dto = new ProcessFileDetailsDTO();

            for(ProcessFileDetailsBO bo : bos){
                dto = new ProcessFileDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setFileName(bo.getFile_Name());
                dto.setFileSize(bo.getFile_Size());
                dto.setInterfaceId(bo.getInterface_Id());
                dto.setFileRowCount(bo.getFile_Row_Count());
                dto.setLoadRowCount(bo.getLoad_Row_Count());
                dto.setLoadDateTime(bo.getLoad_Date_Time());
                dto.setLoadStatus(bo.getLoad_Status());
                dto.setValidationFailureRowCount(bo.getValidation_Failure_Row_Count());
                dto.setValidationStatus(bo.getValidation_Status());
                dto.setValidationDateTime(bo.getValidation_Date_Time());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());
                dto.setAttribute6(bo.getAttribute6());
                dto.setAttribute7(bo.getAttribute7());
                dto.setAttribute8(bo.getAttribute8());
                dto.setAttribute9(bo.getAttribute9());
                dto.setAttribute10(bo.getAttribute10());
                dto.setAttribute11(bo.getAttribute11());
                dto.setAttribute12(bo.getAttribute12());
                dto.setAttribute13(bo.getAttribute13());
                dto.setAttribute14(bo.getAttribute14());
                dto.setAttribute15(bo.getAttribute15());
                dto.setProcessTime(bo.getProcess_Time());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Process File Details BOs failed." +e.getMessage(), e);
        }
    }

    @Override
    public List<ProcessFileDetailsDTO> getProcessFileDetailsExactDTOs(String filename) throws OracleServiceException {
        try {
            List<ProcessFileDetailsBO> bos = new ArrayList<>();
            List<ProcessFileDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getProcessFileDetailsExactBOs(filename);

            ProcessFileDetailsDTO dto = new ProcessFileDetailsDTO();

            for(ProcessFileDetailsBO bo : bos){
                dto = new ProcessFileDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setFileName(bo.getFile_Name());
                dto.setFileSize(bo.getFile_Size());
                dto.setInterfaceId(bo.getInterface_Id());
                dto.setFileRowCount(bo.getFile_Row_Count());
                dto.setLoadRowCount(bo.getLoad_Row_Count());
                dto.setLoadDateTime(bo.getLoad_Date_Time());
                dto.setLoadStatus(bo.getLoad_Status());
                dto.setValidationFailureRowCount(bo.getValidation_Failure_Row_Count());
                dto.setValidationStatus(bo.getValidation_Status());
                dto.setValidationDateTime(bo.getValidation_Date_Time());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());
                dto.setAttribute6(bo.getAttribute6());
                dto.setAttribute7(bo.getAttribute7());
                dto.setAttribute8(bo.getAttribute8());
                dto.setAttribute9(bo.getAttribute9());
                dto.setAttribute10(bo.getAttribute10());
                dto.setAttribute11(bo.getAttribute11());
                dto.setAttribute12(bo.getAttribute12());
                dto.setAttribute13(bo.getAttribute13());
                dto.setAttribute14(bo.getAttribute14());
                dto.setAttribute15(bo.getAttribute15());
                dto.setProcessTime(bo.getProcess_Time());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Process File Details Exact BOs failed.", e);
        }
    }

    @Override
    public List<ProcessFileDetailsFilteredDTO> getProcessFileDetailsFilteredDTOs(String filename) throws OracleServiceException {
        try {
            List<ProcessFileDetailsFilteredBO> bos = new ArrayList<>();
            List<ProcessFileDetailsFilteredDTO> dtoList = new ArrayList<>();
            
            bos = repo.getProcessFileDetailsFilteredBOs(filename);

            ProcessFileDetailsFilteredDTO dto = new ProcessFileDetailsFilteredDTO();

            for(ProcessFileDetailsFilteredBO bo : bos){
                dto = new ProcessFileDetailsFilteredDTO();
                dto.setFileName(bo.getFile_Name());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute4(bo.getAttribute4());
                dto.setLoadStatus(bo.getLoad_Status());
                dto.setProcessTime(bo.getProcess_Time());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Process File Details Filtered BOs failed.", e);
        }
    }

    @Override
    public List<ProcessInstanceDTO> getProcessInstanceByDescDTOs(String filename) throws OracleServiceException {
        try {
            List<ProcessInstanceBO> bos = new ArrayList<>();
            List<ProcessInstanceDTO> dtoList = new ArrayList<>();
            
            bos = repo.getProcessInstanceByDescBOs(filename);

            ProcessInstanceDTO dto = new ProcessInstanceDTO();

            for(ProcessInstanceBO bo : bos){
                dto = new ProcessInstanceDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationName(bo.getIntegration_Name());
                dto.setIntegrationPattern(bo.getIntegration_Pattern());
                dto.setRunDate(bo.getRun_Date());
                dto.setStartTime(bo.getStart_Time());
                dto.setEndTime(bo.getEnd_Time());
                dto.setStatusTime(bo.getStatus_Time());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());
                dto.setAttribute6(bo.getAttribute6());
                dto.setAttribute7(bo.getAttribute7());
                dto.setAttribute8(bo.getAttribute8());
                dto.setAttribute9(bo.getAttribute9());
                dto.setAttribute10(bo.getAttribute10());
                dto.setAttribute11(bo.getAttribute11());
                dto.setAttribute12(bo.getAttribute12());
                dto.setAttribute13(bo.getAttribute13());
                dto.setAttribute14(bo.getAttribute14());
                dto.setAttribute15(bo.getAttribute15());
                dto.setStatus(bo.getStatus());
                dto.setAicInstanceId(bo.getAic_Instance_Id());
                dto.setTransformationStatus(bo.getTransformation_Status());
                dto.setStartBatch(bo.getStart_Batch());
                dto.setEndBatch(bo.getEnd_Batch());
                dto.setInterfaceId(bo.getInterface_Id());
                dto.setTransformationMsg(bo.getTransformation_Msg());
                dto.setBatchName(bo.getBatch_Name());
                dto.setParentInstanceId(bo.getParent_Instance_Id());
                dto.setProcessTime(bo.getProcess_Time());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Process Instance By Desc BOs failed.", e);
        }
    }

    @Override
    public List<ProcessInstanceDTO> getProcessInstanceDTOs(String filename) throws OracleServiceException {
        try {
            List<ProcessInstanceBO> bos = new ArrayList<>();
            List<ProcessInstanceDTO> dtoList = new ArrayList<>();
            
            bos = repo.getProcessInstanceBOs(filename);

            ProcessInstanceDTO dto = new ProcessInstanceDTO();

            for(ProcessInstanceBO bo : bos){
                dto = new ProcessInstanceDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationName(bo.getIntegration_Name());
                dto.setIntegrationPattern(bo.getIntegration_Pattern());
                dto.setRunDate(bo.getRun_Date());
                dto.setStartTime(bo.getStart_Time());
                dto.setEndTime(bo.getEnd_Time());
                dto.setStatusTime(bo.getStatus_Time());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());
                dto.setAttribute6(bo.getAttribute6());
                dto.setAttribute7(bo.getAttribute7());
                dto.setAttribute8(bo.getAttribute8());
                dto.setAttribute9(bo.getAttribute9());
                dto.setAttribute10(bo.getAttribute10());
                dto.setAttribute11(bo.getAttribute11());
                dto.setAttribute12(bo.getAttribute12());
                dto.setAttribute13(bo.getAttribute13());
                dto.setAttribute14(bo.getAttribute14());
                dto.setAttribute15(bo.getAttribute15());
                dto.setStatus(bo.getStatus());
                dto.setAicInstanceId(bo.getAic_Instance_Id());
                dto.setTransformationStatus(bo.getTransformation_Status());
                dto.setStartBatch(bo.getStart_Batch());
                dto.setEndBatch(bo.getEnd_Batch());
                dto.setInterfaceId(bo.getInterface_Id());
                dto.setTransformationMsg(bo.getTransformation_Msg());
                dto.setBatchName(bo.getBatch_Name());
                dto.setParentInstanceId(bo.getParent_Instance_Id());
                dto.setProcessTime(bo.getProcess_Time());

                dtoList.add(dto);
            }
            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Process Instance BOs failed.", e);
        }
    }

    @Override
    public List<OutboundProcessDetailsDTO> getArCustProcessDetailsDTOs() throws OracleServiceException {
        try {
            List<OutboundProcessDetailsBO> bos = new ArrayList<>();
            List<OutboundProcessDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getArCustProcessDetailsBOs();

            OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

            for(OutboundProcessDetailsBO bo : bos){
                dto = new OutboundProcessDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationId(bo.getIntegration_Id());
                dto.setStatus(bo.getStatus());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStartDate(bo.getStart_Date());
                dto.setEndDate(bo.getEnd_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows AR Customer Process Details BOs failed.", e);
        }
    }

    @Override
    public List<OutboundProcessDetailsDTO> getCrosswalkProcessDetailsDTOs() throws OracleServiceException {
        try {
            List<OutboundProcessDetailsBO> bos = new ArrayList<>();
            List<OutboundProcessDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getArCustProcessDetailsBOs();

            OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

            for(OutboundProcessDetailsBO bo : bos){
                dto = new OutboundProcessDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationId(bo.getIntegration_Id());
                dto.setStatus(bo.getStatus());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStartDate(bo.getStart_Date());
                dto.setEndDate(bo.getEnd_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows AR Customer Process Details BOs failed.", e);
        }
    }

    @Override
    public List<OutboundProcessDetailsDTO> getCustBalProcessDetailsDTOs() throws OracleServiceException {
        try {
            List<OutboundProcessDetailsBO> bos = new ArrayList<>();
            List<OutboundProcessDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getArCustProcessDetailsBOs();

            OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

            for(OutboundProcessDetailsBO bo : bos){
                dto = new OutboundProcessDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationId(bo.getIntegration_Id());
                dto.setStatus(bo.getStatus());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStartDate(bo.getStart_Date());
                dto.setEndDate(bo.getEnd_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows AR Customer Process Details BOs failed.", e);
        }
    }

    @Override
    public List<OutboundProcessDetailsDTO> getCustStatementsProcessDetailsDTOs() throws OracleServiceException {
        try {
            List<OutboundProcessDetailsBO> bos = new ArrayList<>();
            List<OutboundProcessDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getArCustProcessDetailsBOs();

            OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

            for(OutboundProcessDetailsBO bo : bos){
                dto = new OutboundProcessDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationId(bo.getIntegration_Id());
                dto.setStatus(bo.getStatus());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStartDate(bo.getStart_Date());
                dto.setEndDate(bo.getEnd_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows AR Customer Process Details BOs failed.", e);
        }
    }

    @Override
    public List<OutboundProcessDetailsDTO> getPosPayAndVoidsProcessDetailsDTOs() throws OracleServiceException {
        try {
            List<OutboundProcessDetailsBO> bos = new ArrayList<>();
            List<OutboundProcessDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getArCustProcessDetailsBOs();

            OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

            for(OutboundProcessDetailsBO bo : bos){
                dto = new OutboundProcessDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationId(bo.getIntegration_Id());
                dto.setStatus(bo.getStatus());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStartDate(bo.getStart_Date());
                dto.setEndDate(bo.getEnd_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows AR Customer Process Details BOs failed.", e);
        }
    }

    @Override
    public List<OutboundProcessDetailsDTO> getPrgExtractsProcessDetailsDTOs() throws OracleServiceException {
        try {
            List<OutboundProcessDetailsBO> bos = new ArrayList<>();
            List<OutboundProcessDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getArCustProcessDetailsBOs();

            OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

            for(OutboundProcessDetailsBO bo : bos){
                dto = new OutboundProcessDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationId(bo.getIntegration_Id());
                dto.setStatus(bo.getStatus());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStartDate(bo.getStart_Date());
                dto.setEndDate(bo.getEnd_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows AR Customer Process Details BOs failed.", e);
        }
    }

    @Override
    public List<OutboundProcessDetailsDTO> getSDChecksProcessDetailsDTOs() throws OracleServiceException {
        try {
            List<OutboundProcessDetailsBO> bos = new ArrayList<>();
            List<OutboundProcessDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getArCustProcessDetailsBOs();

            OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

            for(OutboundProcessDetailsBO bo : bos){
                dto = new OutboundProcessDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationId(bo.getIntegration_Id());
                dto.setStatus(bo.getStatus());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStartDate(bo.getStart_Date());
                dto.setEndDate(bo.getEnd_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows AR Customer Process Details BOs failed.", e);
        }
    }

    @Override
    public List<OutboundProcessDetailsDTO> getUwareProcessDetailsDTOs() throws OracleServiceException {
        try {
            List<OutboundProcessDetailsBO> bos = new ArrayList<>();
            List<OutboundProcessDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getArCustProcessDetailsBOs();

            OutboundProcessDetailsDTO dto = new OutboundProcessDetailsDTO();

            for(OutboundProcessDetailsBO bo : bos){
                dto = new OutboundProcessDetailsDTO();
                dto.setInstanceId(bo.getInstance_Id());
                dto.setIntegrationId(bo.getIntegration_Id());
                dto.setStatus(bo.getStatus());
                dto.setProcessTime(bo.getProcess_Time());
                dto.setStartDate(bo.getStart_Date());
                dto.setEndDate(bo.getEnd_Date());
                dto.setAttribute1(bo.getAttribute1());
                dto.setAttribute2(bo.getAttribute2());
                dto.setAttribute3(bo.getAttribute3());
                dto.setAttribute4(bo.getAttribute4());
                dto.setAttribute5(bo.getAttribute5());

                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows AR Customer Process Details BOs failed.", e);
        }
    }

   


}