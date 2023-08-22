package com.albertsons.argus.domaindb.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.data.bo.oracle.custom.OracleCountCustomBO;
import com.albertsons.argus.data.repo.oracle.CountOracleTablesCustomRepo;
import com.albertsons.argus.domaindb.dto.OracleDTO;
import com.albertsons.argus.domaindb.exception.OracleServiceException;
import com.albertsons.argus.domaindb.service.OracleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OracleServiceImpl implements OracleService {
    @Autowired
    private CountOracleTablesCustomRepo repo;

    @Override
    public List<OracleDTO> getOracleDTO() throws OracleServiceException {

        try {

            List<OracleCountCustomBO> bos = new ArrayList<>();
            List<OracleDTO> dtoList = new ArrayList<>();
            
            bos = repo.getOracleDBCountCustomBOs();

            OracleDTO oracleDTO = new OracleDTO();

            for(OracleCountCustomBO bo : bos){
                oracleDTO = new OracleDTO();
                oracleDTO.setTableName(bo.getTableNm());
                oracleDTO.setRowCount(bo.getBoCount());

                dtoList.add(oracleDTO);
            }

            return dtoList;

        } catch (Exception e){
            throw new OracleServiceException("Retrieving Oracle rows failed.", e);
        }
    }
}