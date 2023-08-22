package com.albertsons.argus.domaindb.service;

import java.util.List;

import com.albertsons.argus.domaindb.dto.OracleDTO;
import com.albertsons.argus.domaindb.exception.OracleServiceException;

public interface OracleService {

    public List<OracleDTO> getOracleDTO() throws OracleServiceException;

}
