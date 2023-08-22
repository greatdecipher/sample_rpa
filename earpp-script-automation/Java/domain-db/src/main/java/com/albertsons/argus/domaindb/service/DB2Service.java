package com.albertsons.argus.domaindb.service;

import java.util.List;

import com.albertsons.argus.domaindb.dto.DB2DTO;
import com.albertsons.argus.domaindb.exception.DB2ServiceException;

public interface DB2Service {

    public List<DB2DTO> getDB2DTO() throws DB2ServiceException;

}