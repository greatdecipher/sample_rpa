package com.albertsons.argus.domaindbr2r.service;

import java.util.List;

import com.albertsons.argus.domaindbr2r.dto.ProcessFileDetailsDTO;
import com.albertsons.argus.domaindbr2r.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbr2r.dto.RecordDTO;
import com.albertsons.argus.domaindbr2r.exception.OracleServiceException;

public interface R2ROracleService {

    public List<ProcessFileDetailsDTO> getProcessFileDetailsDTOs(String filename) throws OracleServiceException;

    public List<RecordDTO> getRecordDTOs(String filename) throws OracleServiceException;

    public List<ProcessInstanceDTO> getProcessInstanceDTOs(String filename) throws OracleServiceException;

}
