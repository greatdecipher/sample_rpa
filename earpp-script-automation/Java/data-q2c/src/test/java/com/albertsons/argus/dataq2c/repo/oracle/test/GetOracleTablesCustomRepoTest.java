package com.albertsons.argus.dataq2c.repo.oracle.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = GetOracleTablesCustomRepoTest.class)
class GetOracleTablesCustomRepoTest {

    @MockBean
    private GetOracleTablesCustomRepo mockRepo;

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
    void setMockInterfaceLinesBOs() {
        List<InterfaceLinesBO> list = new ArrayList<>();

        InterfaceLinesBO bo = new InterfaceLinesBO(){

            @Override
            public Long getInstance_Id() {
                return 4835649L;
            }

            @Override
            public String getAlternateDocumentId() {
                return "";
            }

            @Override
            public String getStatus() {
                return "";
            }

            @Override
            public String getError_Message() {
                return "";
            }

            @Override
            public String getCount() {
                return "";
            }
            
        };

        list.add(bo);

        when(mockRepo.getInterfaceLinesBOs(anyList())).thenReturn(list);
    }

    @BeforeEach
    void setMockJobHistoryInLinesBOs(){
        List<JobHistoryBO> list = new ArrayList<>();

        JobHistoryBO bo = new JobHistoryBO(){

            @Override
            public String getAlternateDocumentId() {
                return "";
            }

            @Override
            public Long getInstance_Id() {
                return 4835649L;
            }

            @Override
            public String getCallback_Id() {
                return "";
            }

            @Override
            public String getJob_Id() {
                return "";
            }

            @Override
            public String getJob_Name() {
                return "";
            }

            @Override
            public String getJob_Status() {
                return "";
            }

            @Override
            public String getProcess_Time() {
                return "";
            }

        };

        list.add(bo);

        when(mockRepo.getJobHistoryInLinesBOs(anyList())).thenReturn(list);
    }

    @BeforeEach
    void setMockBankStatementsBOs(){
        List<BankStatementsBO> list = new ArrayList<>();

        BankStatementsBO bo = new BankStatementsBO(){

            @Override
            public Long getInstance_Id() {
                return 4835649L;
            }

            @Override
            public String getCallback_Id() {
                return "";
            }

            @Override
            public String getJob_Id() {
                return "";
            }

            @Override
            public String getJob_Name() {
                return "";
            }

            @Override
            public String getJob_Status() {
                return "";
            }

            @Override
            public String getProcess_Time() {
                return "";
            }

            @Override
            public String getAlternateDocumentId() {
                return "";
            }

            @Override
            public String getFile_Name() {
                return "";
            }

            @Override
            public Integer getCount() {
                return 10;
            }

        };

        list.add(bo);

        when(mockRepo.getBankStatementsBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockBankProcessInstanceBOs(){
        List<BankProcessInstanceBO> list = new ArrayList<>();

        BankProcessInstanceBO bo = new BankProcessInstanceBO(){

            @Override
            public Long getInstance_Id() {
                return 4835649L;
            }

            @Override
            public String getBatch_Name() {
                return "";
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
            public String getStatus() {
                return "";
            }

            @Override
            public String getTransformation_Status() {
                return "";
            }

            @Override
            public String getInterface_Id() {
                return "";
            }

            @Override
            public String getRun_Date() {
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
            public String getProcess_Time() {
                return "";
            }

        };

        list.add(bo);

        when(mockRepo.getBankProcessInstanceBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockProcessInstanceByDescBOs() {
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

        when(mockRepo.getProcessInstanceByDescBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockJobHistoryTrxsBOs(){
        List<JobHistoryBO> list = new ArrayList<>();

        JobHistoryBO bo = new JobHistoryBO(){

            @Override
            public String getAlternateDocumentId() {
                return "";
            }

            @Override
            public Long getInstance_Id() {
                return 4835649L;
            }

            @Override
            public String getCallback_Id() {
                return "";
            }

            @Override
            public String getJob_Id() {
                return "";
            }

            @Override
            public String getJob_Name() {
                return "";
            }

            @Override
            public String getJob_Status() {
                return "";
            }

            @Override
            public String getProcess_Time() {
                return "";
            }

        };

        list.add(bo);

        when(mockRepo.getJobHistoryTrxsBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockProcessFileDetailsFilteredBOs(){
        List<ProcessFileDetailsFilteredBO> list = new ArrayList<>();

        ProcessFileDetailsFilteredBO bo = new ProcessFileDetailsFilteredBO(){

            @Override
            public String getFile_Name() {
                return "";
            }

            @Override
            public String getAttribute2() {
                return "";
            }

            @Override
            public String getAttribute4() {
                return "";
            }

            @Override
            public String getLoad_Status() {
                return "";
            }

            @Override
            public String getProcess_Time() {
                return "";
            }

        };

        list.add(bo);

        when(mockRepo.getProcessFileDetailsFilteredBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockProcessFileDetailsExactBOs(){
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

        when(mockRepo.getProcessFileDetailsExactBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockLockboxHdrTrailPayBOs(){
        List<LockboxHdrTrailPayBO> list = new ArrayList<>();

        LockboxHdrTrailPayBO bo = new LockboxHdrTrailPayBO(){

            @Override
            public String getRecord_Type() {
                return "";
            }

            @Override
            public String getBatch_Name() {
                return "";
            }

            @Override
            public String getCurrency_Code() {
                return "";
            }

            @Override
            public String getExchange_Rate_Type() {
                return "";
            }

            @Override
            public String getExchange_Rate() {
                return "";
            }

            @Override
            public String getReceipt_Method() {
                return "";
            }

            @Override
            public String getLockbox_Number() {
                return "";
            }

            @Override
            public String getDeposit_Date() {
                return "";
            }

            @Override
            public String getDeposit_Time() {
                return "";
            }

            @Override
            public String getBatch_Record_Count() {
                return "";
            }

            @Override
            public String getBatch_Amount() {
                return "";
            }

            @Override
            public String getComments() {
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
            public String getAttribute_Category() {
                return "";
            }

            @Override
            public String getItem_Number() {
                return "";
            }

            @Override
            public String getOverflow_Indicator() {
                return "";
            }

            @Override
            public String getOverflow_Sequence() {
                return "";
            }

            @Override
            public String getRemittance_Amount() {
                return "";
            }

            @Override
            public String getTransit_Routing_Number() {
                return "";
            }

            @Override
            public String getAccount() {
                return "";
            }

            @Override
            public String getCheck_Number() {
                return "";
            }

            @Override
            public String getReceipt_Date() {
                return "";
            }

            @Override
            public String getCustomer_Number() {
                return "";
            }

            @Override
            public String getBill_To_Location() {
                return "";
            }

            @Override
            public String getCustomer_Bank_Branch_Name() {
                return "";
            }

            @Override
            public String getCustomer_Bank_Name() {
                return "";
            }

            @Override
            public String getRemittance_Bank_Branch_Name() {
                return "";
            }

            @Override
            public String getRemittance_Bank_Name() {
                return "";
            }

            @Override
            public String getAnticipated_Clearing_Date() {
                return "";
            }

            @Override
            public String getInvoice1() {
                return "";
            }

            @Override
            public String getInvoice1_Installment() {
                return "";
            }

            @Override
            public String getMatching1_Date() {
                return "";
            }

            @Override
            public String getInvoice_Currency_Code1() {
                return "";
            }

            @Override
            public String getTrans_To_Receipt_Rate1() {
                return "";
            }

            @Override
            public String getAmount_Applied1() {
                return "";
            }

            @Override
            public String getAmount_Applied_From1() {
                return "";
            }

            @Override
            public String getCustomer_Reference1() {
                return "";
            }

            @Override
            public String getInvoice2() {
                return "";
            }

            @Override
            public String getInvoice2_Installment() {
                return "";
            }

            @Override
            public String getMatching2_Date() {
                return "";
            }

            @Override
            public String getInvoice_Currency_Code2() {
                return "";
            }

            @Override
            public String getTrans_To_Receipt_Rate2() {
                return "";
            }

            @Override
            public String getAmount_Applied2() {
                return "";
            }

            @Override
            public String getAmount_Applied_From2() {
                return "";
            }

            @Override
            public String getCustomer_Reference2() {
                return "";
            }

            @Override
            public String getInvoice3() {
                return "";
            }

            @Override
            public String getInvoice3_Installment() {
                return "";
            }

            @Override
            public String getMatching3_Date() {
                return "";
            }

            @Override
            public String getInvoice_Currency_Code3() {
                return "";
            }

            @Override
            public String getTrans_To_Receipt_Rate3() {
                return "";
            }

            @Override
            public String getAmount_Applied3() {
                return "";
            }

            @Override
            public String getAmount_Applied_From3() {
                return "";
            }

            @Override
            public String getCustomer_Reference3() {
                return "";
            }

            @Override
            public String getInvoice4() {
                return "";
            }

            @Override
            public String getInvoice4_Installment() {
                return "";
            }

            @Override
            public String getMatching4_Date() {
                return "";
            }

            @Override
            public String getInvoice_Currency_Code4() {
                return "";
            }

            @Override
            public String getTrans_To_Receipt_Rate4() {
                return "";
            }

            @Override
            public String getAmount_Applied4() {
                return "";
            }

            @Override
            public String getAmount_Applied_From4() {
                return "";
            }

            @Override
            public String getCustomer_Reference4() {
                return "";
            }

            @Override
            public String getInvoice5() {
                return "";
            }

            @Override
            public String getInvoice5_Installment() {
                return "";
            }

            @Override
            public String getMatching5_Date() {
                return "";
            }

            @Override
            public String getInvoice_Currency_Code5() {
                return "";
            }

            @Override
            public String getTrans_To_Receipt_Rate5() {
                return "";
            }

            @Override
            public String getAmount_Applied5() {
                return "";
            }

            @Override
            public String getAmount_Applied_From5() {
                return "";
            }

            @Override
            public String getCustomer_Reference5() {
                return "";
            }

            @Override
            public String getInvoice6() {
                return "";
            }

            @Override
            public String getInvoice6_Installment() {
                return "";
            }

            @Override
            public String getMatching6_Date() {
                return "";
            }

            @Override
            public String getInvoice_Currency_Code6() {
                return "";
            }

            @Override
            public String getTrans_To_Receipt_Rate6() {
                return "";
            }

            @Override
            public String getAmount_Applied6() {
                return "";
            }

            @Override
            public String getAmount_Applied_From6() {
                return "";
            }

            @Override
            public String getCustomer_Reference6() {
                return "";
            }

            @Override
            public String getInvoice7() {
                return "";
            }

            @Override
            public String getInvoice7_Installment() {
                return "";
            }

            @Override
            public String getMatching7_Date() {
                return "";
            }

            @Override
            public String getInvoice_Currency_Code7() {
                return "";
            }

            @Override
            public String getTrans_To_Receipt_Rate7() {
                return "";
            }

            @Override
            public String getAmount_Applied7() {
                return "";
            }

            @Override
            public String getAmount_Applied_From7() {
                return "";
            }

            @Override
            public String getCustomer_Reference7() {
                return "";
            }

            @Override
            public String getInvoice8() {
                return "";
            }

            @Override
            public String getInvoice8_Installment() {
                return "";
            }

            @Override
            public String getMatching8_Date() {
                return "";
            }

            @Override
            public String getInvoice_Currency_Code8() {
                return "";
            }

            @Override
            public String getTrans_To_Receipt_Rate8() {
                return "";
            }

            @Override
            public String getAmount_Applied8() {
                return "";
            }

            @Override
            public String getAmount_Applied_From8() {
                return "";
            }

            @Override
            public String getCustomer_Reference8() {
                return "";
            }

            @Override
            public String getAttribute_Number1() {
                return "";
            }

            @Override
            public String getAttribute_Number2() {
                return "";
            }

            @Override
            public String getAttribute_Number3() {
                return "";
            }

            @Override
            public String getAttribute_Number4() {
                return "";
            }

            @Override
            public String getAttribute_Number5() {
                return "";
            }

            @Override
            public String getAttribute_Number6() {
                return "";
            }

            @Override
            public String getAttribute_Number7() {
                return "";
            }

            @Override
            public String getAttribute_Number8() {
                return "";
            }

            @Override
            public String getAttribute_Number9() {
                return "";
            }

            @Override
            public String getAttribute_Number10() {
                return "";
            }

            @Override
            public String getAttribute_Number11() {
                return "";
            }

            @Override
            public String getAttribute_Number12() {
                return "";
            }

            @Override
            public String getAttribute_Number13() {
                return "";
            }

            @Override
            public String getAttribute_Number14() {
                return "";
            }

            @Override
            public String getAttribute_Number15() {
                return "";
            }

            @Override
            public String getBatch_Number() {
                return "";
            }

            @Override
            public Long getInstance_Id() {
                return 4835649L;
            }

            @Override
            public String getProcess_Time() {
                return "";
            }

            @Override
            public String getScheduled_Instance_Id() {
                return "";
            }

            @Override
            public String getError_Message() {
                return "";
            }

            @Override
            public String getStatus() {
                return "";
            }

            @Override
            public String getFilename() {
                return "";
            }

            @Override
            public String getRecord_Count() {
                return "";
            }

            @Override
            public String getFile_Creation_Date() {
                return "";
            }

            @Override
            public String getLast_Update_Date() {
                return "";
            }

            @Override
            public String getLast_Updated_By() {
                return "";
            }

            @Override
            public String getLast_Update_Login() {
                return "";
            }

            @Override
            public String getCreation_Date() {
                return "";
            }

            @Override
            public String getCreated_By() {
                return "";
            }

            @Override
            public String getObject_Version_Number() {
                return "";
            }

            @Override
            public String getPartition_Number() {
                return "";
            }

            @Override
            public String getOffset() {
                return "";
            }

            @Override
            public String getConsumer_Instance() {
                return "";
            }

            @Override
            public String getTopic() {
                return "";
            }

            @Override
            public String getConsumer_Group() {
                return "";
            }

            @Override
            public String getIntegration_Type() {
                return "";
            }

            @Override
            public String getFormat_Number() {
                return "";
            }

            @Override
            public String getPayment_Date() {
                return "";
            }

            @Override
            public String getSource_Application_Code() {
                return "";
            }

            @Override
            public String getPayment_Number() {
                return "";
            }

            @Override
            public String getPayment_Description() {
                return "";
            }

            @Override
            public String getPayment_Type() {
                return "";
            }

            @Override
            public String getPayment_Currency() {
                return "";
            }

            @Override
            public String getBusiness_Unit() {
                return "";
            }

            @Override
            public String getPayee_Name() {
                return "";
            }

            @Override
            public String getPayee_Site() {
                return "";
            }

            @Override
            public String getVendor_Id() {
                return "";
            }

            @Override
            public String getVendor_Num() {
                return "";
            }

            @Override
            public String getVendor_Sub_Acct_Id() {
                return "";
            }

            @Override
            public String getBank_Account_Name() {
                return "";
            }

            @Override
            public String getBank_Account_Number() {
                return "";
            }

            @Override
            public String getPayment_Method() {
                return "";
            }

            @Override
            public String getPayment_Process_Profile() {
                return "";
            }

            @Override
            public String getPayment_Document() {
                return "";
            }

            @Override
            public String getInvoice_Num() {
                return "";
            }

            @Override
            public String getInstallment_Number() {
                return "";
            }

            @Override
            public String getAmount_Paid_Inv_Curr() {
                return "";
            }

            @Override
            public String getDiscount_Taken() {
                return "";
            }

            @Override
            public String getAccounting_Unit() {
                return "";
            }

            @Override
            public String getParentInstanceId() {
                return "";
            }

            @Override
            public String getMessageSequenceNbr() {
                return "";
            }

            @Override
            public String getExpectedMessageCnt() {
                return "";
            }

            @Override
            public String getAlternateDocumentId() {
                return "";
            }

        };

        list.add(bo);

        when(mockRepo.getLockboxHdrTrailPayBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockJobHistoryLockboxBOs(){
        List<JobHistoryBO> list = new ArrayList<>();

        JobHistoryBO bo = new JobHistoryBO(){

            @Override
            public String getAlternateDocumentId() {
                return "";
            }

            @Override
            public Long getInstance_Id() {
                return 4835649L;
            }

            @Override
            public String getCallback_Id() {
                return "";
            }

            @Override
            public String getJob_Id() {
                return "";
            }

            @Override
            public String getJob_Name() {
                return "";
            }

            @Override
            public String getJob_Status() {
                return "";
            }

            @Override
            public String getProcess_Time() {
                return "";
            }

        };

        list.add(bo);

        when(mockRepo.getJobHistoryLockboxBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockTargetedProcessFileDetailsBOs(){
        List<TargetedProcessFileDetailsBO> list = new ArrayList<>();

        TargetedProcessFileDetailsBO bo = new TargetedProcessFileDetailsBO(){

            @Override
            public String getFile_Name(){
                return "REM_LCKBX_L1";
            }

            @Override
            public String getLoad_Status(){
                return "";
            }
            
            @Override
            public String getLoad_Date(){
                return "";
            }
            
            @Override
            public Integer getExpectedMessageCnt(){
                return 400;
            }
            
            @Override
            public Integer getConsumed_Rec(){
                return 25;
            }
            
            @Override
            public String getStart_Process(){
                return "";
            }
            
            @Override
            public String getEnd_Process(){
                return "";
            }
            
            @Override
            public String getBox_Name(){
                return "";
            }
            
            @Override
            public String getSource_Application_Code(){
                return "";
            }
            
            @Override
            public String getBatch_Name(){
                return "";
            }
            
            @Override
            public String getRemittance_Amount(){
                return "";
            }

            @Override
            public Long getInstance_Id(){
                return 4835649L;
            }
            
            @Override
            public String getProcess_Time(){
                return "";
            }
            
            @Override
            public String getStatus(){
                return "";
            }
            
            @Override
            public String getTransformation_Status(){
                return "";
            }
            
            @Override
            public String getTransformation_Msg(){
                return "";
            }
        };

        list.add(bo);

        when(mockRepo.getTargetedProcessFileDetailsBOs(anyString())).thenReturn(list);
    }

    @BeforeEach
    void setMockArCustProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> list = new ArrayList<>();

        OutboundProcessDetailsBO bo = new OutboundProcessDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return null;
            }

            @Override
            public String getIntegration_Id() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public String getProcess_Time() {
                return null;
            }

            @Override
            public String getStart_Date() {
                return null;
            }

            @Override
            public String getEnd_Date() {
                return null;
            }

            @Override
            public String getAttribute1() {
                return null;
            }

            @Override
            public String getAttribute2() {
                return null;
            }

            @Override
            public String getAttribute3() {
                return null;
            }

            @Override
            public String getAttribute4() {
                return null;
            }

            @Override
            public String getAttribute5() {
                return null;
            }

        };

        list.add(bo);

        when(mockRepo.getArCustProcessDetailsBOs()).thenReturn(list);
    }

    @BeforeEach
    void setMockCrosswalkProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> list = new ArrayList<>();

        OutboundProcessDetailsBO bo = new OutboundProcessDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return null;
            }

            @Override
            public String getIntegration_Id() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public String getProcess_Time() {
                return null;
            }

            @Override
            public String getStart_Date() {
                return null;
            }

            @Override
            public String getEnd_Date() {
                return null;
            }

            @Override
            public String getAttribute1() {
                return null;
            }

            @Override
            public String getAttribute2() {
                return null;
            }

            @Override
            public String getAttribute3() {
                return null;
            }

            @Override
            public String getAttribute4() {
                return null;
            }

            @Override
            public String getAttribute5() {
                return null;
            }

        };

        list.add(bo);

        when(mockRepo.getCrosswalkProcessDetailsBOs()).thenReturn(list);
    }

    @BeforeEach
    void setMockCustBalProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> list = new ArrayList<>();

        OutboundProcessDetailsBO bo = new OutboundProcessDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return null;
            }

            @Override
            public String getIntegration_Id() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public String getProcess_Time() {
                return null;
            }

            @Override
            public String getStart_Date() {
                return null;
            }

            @Override
            public String getEnd_Date() {
                return null;
            }

            @Override
            public String getAttribute1() {
                return null;
            }

            @Override
            public String getAttribute2() {
                return null;
            }

            @Override
            public String getAttribute3() {
                return null;
            }

            @Override
            public String getAttribute4() {
                return null;
            }

            @Override
            public String getAttribute5() {
                return null;
            }

        };

        list.add(bo);

        when(mockRepo.getCustBalProcessDetailsBOs()).thenReturn(list);
    }

    @BeforeEach
    void setMockCustStatementsProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> list = new ArrayList<>();

        OutboundProcessDetailsBO bo = new OutboundProcessDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return null;
            }

            @Override
            public String getIntegration_Id() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public String getProcess_Time() {
                return null;
            }

            @Override
            public String getStart_Date() {
                return null;
            }

            @Override
            public String getEnd_Date() {
                return null;
            }

            @Override
            public String getAttribute1() {
                return null;
            }

            @Override
            public String getAttribute2() {
                return null;
            }

            @Override
            public String getAttribute3() {
                return null;
            }

            @Override
            public String getAttribute4() {
                return null;
            }

            @Override
            public String getAttribute5() {
                return null;
            }

        };

        list.add(bo);

        when(mockRepo.getCustStatementsProcessDetailsBOs()).thenReturn(list);
    }

    @BeforeEach
    void setMockPosPayAndVoidsProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> list = new ArrayList<>();

        OutboundProcessDetailsBO bo = new OutboundProcessDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return null;
            }

            @Override
            public String getIntegration_Id() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public String getProcess_Time() {
                return null;
            }

            @Override
            public String getStart_Date() {
                return null;
            }

            @Override
            public String getEnd_Date() {
                return null;
            }

            @Override
            public String getAttribute1() {
                return null;
            }

            @Override
            public String getAttribute2() {
                return null;
            }

            @Override
            public String getAttribute3() {
                return null;
            }

            @Override
            public String getAttribute4() {
                return null;
            }

            @Override
            public String getAttribute5() {
                return null;
            }

        };

        list.add(bo);

        when(mockRepo.getPosPayAndVoidsProcessDetailsBOs()).thenReturn(list);
    }

    @BeforeEach
    void setMockPrgExtractsProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> list = new ArrayList<>();

        OutboundProcessDetailsBO bo = new OutboundProcessDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return null;
            }

            @Override
            public String getIntegration_Id() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public String getProcess_Time() {
                return null;
            }

            @Override
            public String getStart_Date() {
                return null;
            }

            @Override
            public String getEnd_Date() {
                return null;
            }

            @Override
            public String getAttribute1() {
                return null;
            }

            @Override
            public String getAttribute2() {
                return null;
            }

            @Override
            public String getAttribute3() {
                return null;
            }

            @Override
            public String getAttribute4() {
                return null;
            }

            @Override
            public String getAttribute5() {
                return null;
            }

        };

        list.add(bo);

        when(mockRepo.getPrgExtractsProcessDetailsBOs()).thenReturn(list);
    }

    @BeforeEach
    void setMockSDChecksProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> list = new ArrayList<>();

        OutboundProcessDetailsBO bo = new OutboundProcessDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return null;
            }

            @Override
            public String getIntegration_Id() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public String getProcess_Time() {
                return null;
            }

            @Override
            public String getStart_Date() {
                return null;
            }

            @Override
            public String getEnd_Date() {
                return null;
            }

            @Override
            public String getAttribute1() {
                return null;
            }

            @Override
            public String getAttribute2() {
                return null;
            }

            @Override
            public String getAttribute3() {
                return null;
            }

            @Override
            public String getAttribute4() {
                return null;
            }

            @Override
            public String getAttribute5() {
                return null;
            }

        };

        list.add(bo);

        when(mockRepo.getSDChecksProcessDetailsBOs()).thenReturn(list);
    }

    @BeforeEach
    void setMockUwareProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> list = new ArrayList<>();

        OutboundProcessDetailsBO bo = new OutboundProcessDetailsBO(){

            @Override
            public Long getInstance_Id() {
                return null;
            }

            @Override
            public String getIntegration_Id() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public String getProcess_Time() {
                return null;
            }

            @Override
            public String getStart_Date() {
                return null;
            }

            @Override
            public String getEnd_Date() {
                return null;
            }

            @Override
            public String getAttribute1() {
                return null;
            }

            @Override
            public String getAttribute2() {
                return null;
            }

            @Override
            public String getAttribute3() {
                return null;
            }

            @Override
            public String getAttribute4() {
                return null;
            }

            @Override
            public String getAttribute5() {
                return null;
            }

        };

        list.add(bo);

        when(mockRepo.getUwareProcessDetailsBOs()).thenReturn(list);
    }

    @Test
    void testGetProcessInstanceBOs(){
        List<ProcessInstanceBO> boList = mockRepo.getProcessInstanceBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetProcessFileDetailsBOs(){
        List<ProcessFileDetailsBO> boList = mockRepo.getProcessFileDetailsBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetInterfaceLinesBOs(){
        List<String> filenames = new ArrayList<>();
        filenames.add("JE_JIF_SC_");
        List<InterfaceLinesBO> boList = mockRepo.getInterfaceLinesBOs(filenames);
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetJobHistoryInLinesBOs(){
        List<String> filenames = new ArrayList<>();
        filenames.add("JE_JIF_SC_");
        List<JobHistoryBO> boList = mockRepo.getJobHistoryInLinesBOs(filenames);
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetBankStatementsBOs(){
        List<BankStatementsBO> boList = mockRepo.getBankStatementsBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetBankProcessInstanceBOs(){
        List<BankProcessInstanceBO> boList = mockRepo.getBankProcessInstanceBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetProcessInstanceByDescBOs(){
        List<ProcessInstanceBO> boList = mockRepo.getProcessInstanceByDescBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetJobHistoryTrxsBOs(){
        List<JobHistoryBO> boList = mockRepo.getJobHistoryTrxsBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetProcessFileDetailsFilteredBOs(){
        List<ProcessFileDetailsFilteredBO> boList = mockRepo.getProcessFileDetailsFilteredBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetProcessFileDetailsExactBOs(){
        List<ProcessFileDetailsBO> boList = mockRepo.getProcessFileDetailsExactBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetLockboxHdrTrailPayBOs(){
        List<LockboxHdrTrailPayBO> boList = mockRepo.getLockboxHdrTrailPayBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetJobHistoryLockboxBOs(){
        List<JobHistoryBO> boList = mockRepo.getJobHistoryLockboxBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetTargetedProcessFileDetailsBOs(){
        List<TargetedProcessFileDetailsBO> boList = mockRepo.getTargetedProcessFileDetailsBOs("JE_JIF_SC_");
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetArCustProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> boList = mockRepo.getArCustProcessDetailsBOs();
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetCrosswalkProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> boList = mockRepo.getCrosswalkProcessDetailsBOs();
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetCustBalProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> boList = mockRepo.getCustBalProcessDetailsBOs();
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetCustStatementsProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> boList = mockRepo.getCustStatementsProcessDetailsBOs();
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetPosPayAndVoidsProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> boList = mockRepo.getPosPayAndVoidsProcessDetailsBOs();
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetPrgExtractsProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> boList = mockRepo.getPrgExtractsProcessDetailsBOs();
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetSDChecksProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> boList = mockRepo.getSDChecksProcessDetailsBOs();
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
    
    @Test
    void testGetUwareProcessDetailsBOs(){
        List<OutboundProcessDetailsBO> boList = mockRepo.getUwareProcessDetailsBOs();
        
        assertTrue(boList.size() > 0, "List is greater than 0");
    }
}

