package com.albertsons.argus.domaindbq2c.service;

import java.util.List;

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

public interface DbOracleService {
    public List<BankProcessInstanceDTO> getBankProcessInstanceDTOs(String filename) throws OracleServiceException;
    
    public List<BankStatementsDTO> getBankStatementsDTOs(String filename) throws OracleServiceException;
    
    public List<InterfaceLinesDTO> getInterfaceLinesDTOs(List<String> filenames) throws OracleServiceException;

    public List<InterfaceLinesDTO> getInterfaceLineCountDTOs(String filename) throws OracleServiceException;
    
    public List<JobHistoryDTO> getJobHistoryInLinesDTOs(List<String> filenames) throws OracleServiceException;
    
    public List<JobHistoryDTO> getJobHistoryLockboxDTOs(String filename) throws OracleServiceException;
    
    public List<JobHistoryDTO> getJobHistoryTrxsDTOs(String filename) throws OracleServiceException;
    
    public List<LockboxHdrTrailPayDTO> getLockboxHdrTrailPayDTOs(String filename) throws OracleServiceException;
    
    public List<TargetedProcessFileDetailsDTO> getTargetedProcessFileDetailsDTOs(String filename) throws OracleServiceException;

    public List<ProcessFileDetailsDTO> getProcessFileDetailsDTOs(String filename) throws OracleServiceException;
    
    public List<ProcessFileDetailsDTO> getProcessFileDetailsExactDTOs(String filename) throws OracleServiceException;
    
    public List<ProcessFileDetailsFilteredDTO> getProcessFileDetailsFilteredDTOs(String filename) throws OracleServiceException;
    
    public List<ProcessInstanceDTO> getProcessInstanceByDescDTOs(String filename) throws OracleServiceException;
    
    public List<ProcessInstanceDTO> getProcessInstanceDTOs(String filename) throws OracleServiceException;

    public List<OutboundProcessDetailsDTO> getArCustProcessDetailsDTOs() throws OracleServiceException;

    public List<OutboundProcessDetailsDTO> getCrosswalkProcessDetailsDTOs() throws OracleServiceException;

    public List<OutboundProcessDetailsDTO> getCustBalProcessDetailsDTOs() throws OracleServiceException;

    public List<OutboundProcessDetailsDTO> getCustStatementsProcessDetailsDTOs() throws OracleServiceException;

    public List<OutboundProcessDetailsDTO> getPosPayAndVoidsProcessDetailsDTOs() throws OracleServiceException;

    public List<OutboundProcessDetailsDTO> getPrgExtractsProcessDetailsDTOs() throws OracleServiceException;

    public List<OutboundProcessDetailsDTO> getSDChecksProcessDetailsDTOs() throws OracleServiceException;

    public List<OutboundProcessDetailsDTO> getUwareProcessDetailsDTOs() throws OracleServiceException;
}
