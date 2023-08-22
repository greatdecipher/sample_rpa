package com.albertsons.argus.domaindbr2r.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.datar2r.bo.oracle.custom.ProcessFileDetailsBO;
import com.albertsons.argus.datar2r.bo.oracle.custom.ProcessInstanceBO;
import com.albertsons.argus.datar2r.bo.oracle.custom.RecordBO;
import com.albertsons.argus.datar2r.repo.oracle.GetOracleTablesCustomRepo;
import com.albertsons.argus.domaindbr2r.dto.ProcessFileDetailsDTO;
import com.albertsons.argus.domaindbr2r.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbr2r.dto.RecordDTO;
import com.albertsons.argus.domaindbr2r.exception.OracleServiceException;
import com.albertsons.argus.domaindbr2r.service.R2ROracleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class R2ROracleServiceImpl implements R2ROracleService {
    @Autowired
    private GetOracleTablesCustomRepo repo;

    @Override
    public List<ProcessFileDetailsDTO> getProcessFileDetailsDTOs(String filename) throws OracleServiceException {

        try {

            List<ProcessFileDetailsBO> bos = new ArrayList<>();
            List<ProcessFileDetailsDTO> dtoList = new ArrayList<>();
            
            bos = repo.getProcessFileDetailsBOs(filename);

            ProcessFileDetailsDTO pfdDTO = new ProcessFileDetailsDTO();

            for(ProcessFileDetailsBO bo : bos){
                pfdDTO = new ProcessFileDetailsDTO();
                pfdDTO.setInstance_Id(bo.getInstance_Id());
                pfdDTO.setFile_Name(bo.getFile_Name());
                pfdDTO.setFile_Size(bo.getFile_Size());
                pfdDTO.setInterface_Id(bo.getInterface_Id());
                pfdDTO.setFile_Row_Count(bo.getFile_Row_Count());
                pfdDTO.setLoad_Row_Count(bo.getLoad_Row_Count());
                pfdDTO.setLoad_Date_Time(bo.getLoad_Date_Time());
                pfdDTO.setLoad_Status(bo.getLoad_Status());
                pfdDTO.setValidation_Failure_Row_Count(bo.getValidation_Failure_Row_Count());
                pfdDTO.setValidation_Status(bo.getValidation_Status());
                pfdDTO.setValidation_Date_Time(bo.getValidation_Date_Time());
                pfdDTO.setAttribute1(bo.getAttribute1());
                pfdDTO.setAttribute2(bo.getAttribute2());
                pfdDTO.setAttribute3(bo.getAttribute3());
                pfdDTO.setAttribute4(bo.getAttribute4());
                pfdDTO.setAttribute5(bo.getAttribute5());
                pfdDTO.setAttribute6(bo.getAttribute6());
                pfdDTO.setAttribute7(bo.getAttribute7());
                pfdDTO.setAttribute8(bo.getAttribute8());
                pfdDTO.setAttribute9(bo.getAttribute9());
                pfdDTO.setAttribute10(bo.getAttribute10());
                pfdDTO.setAttribute11(bo.getAttribute11());
                pfdDTO.setAttribute12(bo.getAttribute12());
                pfdDTO.setAttribute13(bo.getAttribute13());
                pfdDTO.setAttribute14(bo.getAttribute14());
                pfdDTO.setAttribute15(bo.getAttribute15());
                pfdDTO.setProcess_Time(bo.getProcess_Time());

                dtoList.add(pfdDTO);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Process File Details BOs failed.", e);
        }
    }

    @Override
    public List<RecordDTO> getRecordDTOs(String filename) throws OracleServiceException {

        try {

            List<RecordBO> bos = new ArrayList<>();
            List<RecordDTO> dtoList = new ArrayList<>();
            
            bos = repo.getRecordBOs(filename);

            RecordDTO recordDTO = new RecordDTO();

            for(RecordBO bo : bos){
                recordDTO = new RecordDTO();
                recordDTO.setGroup_Id(bo.getGroup_Id());
                recordDTO.setBatch_Name(bo.getBatch_Name());
                recordDTO.setTotal_Rec(bo.getTotal_Rec());
                recordDTO.setConsumed_Rec(bo.getConsumed_Rec());
                recordDTO.setRemaining_Rec(bo.getRemaining_Rec());
                recordDTO.setSum_Entered_Dr(bo.getSum_Entered_Dr());
                recordDTO.setSum_Entered_Cr(bo.getSum_Entered_Cr());
                recordDTO.setMin_Process_Time(bo.getMin_Process_Time());
                recordDTO.setMax_Process_Time(bo.getMax_Process_Time());

                dtoList.add(recordDTO);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Record BOs failed.", e);
        }
    }

    @Override
    public List<ProcessInstanceDTO> getProcessInstanceDTOs(String filename) throws OracleServiceException {
        try {

            List<ProcessInstanceBO> bos = new ArrayList<>();
            List<ProcessInstanceDTO> dtoList = new ArrayList<>();
            
            bos = repo.getProcessInstanceBOs(filename);

            ProcessInstanceDTO piDTO = new ProcessInstanceDTO();

            for(ProcessInstanceBO bo : bos){
                piDTO = new ProcessInstanceDTO();
                piDTO.setInstance_Id(bo.getInstance_Id());
                piDTO.setIntegration_Name(bo.getIntegration_Name());
                piDTO.setIntegration_Pattern(bo.getIntegration_Pattern());
                piDTO.setRun_Date(bo.getRun_Date());
                piDTO.setStart_Time(bo.getStart_Time());
                piDTO.setEnd_Time(bo.getEnd_Time());
                piDTO.setStatus_Time(bo.getStatus_Time());
                piDTO.setAttribute1(bo.getAttribute1());
                piDTO.setAttribute2(bo.getAttribute2());
                piDTO.setAttribute3(bo.getAttribute3());
                piDTO.setAttribute4(bo.getAttribute4());
                piDTO.setAttribute5(bo.getAttribute5());
                piDTO.setAttribute6(bo.getAttribute6());
                piDTO.setAttribute7(bo.getAttribute7());
                piDTO.setAttribute8(bo.getAttribute8());
                piDTO.setAttribute9(bo.getAttribute9());
                piDTO.setAttribute10(bo.getAttribute10());
                piDTO.setAttribute11(bo.getAttribute11());
                piDTO.setAttribute12(bo.getAttribute12());
                piDTO.setAttribute13(bo.getAttribute13());
                piDTO.setAttribute14(bo.getAttribute14());
                piDTO.setAttribute15(bo.getAttribute15());
                piDTO.setStatus(bo.getStatus());
                piDTO.setAic_Instance_Id(bo.getAic_Instance_Id());
                piDTO.setTransformation_Status(bo.getTransformation_Status());
                piDTO.setStart_Batch(bo.getStart_Batch());
                piDTO.setEnd_Batch(bo.getEnd_Batch());
                piDTO.setInterface_Id(bo.getInterface_Id());
                piDTO.setTransformation_Msg(bo.getTransformation_Msg());
                piDTO.setBatch_Name(bo.getBatch_Name());
                piDTO.setParent_Instance_Id(bo.getParent_Instance_Id());
                piDTO.setProcess_Time(bo.getProcess_Time());

                dtoList.add(piDTO);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows Process Instance BOs failed.", e);
        }
    }
}