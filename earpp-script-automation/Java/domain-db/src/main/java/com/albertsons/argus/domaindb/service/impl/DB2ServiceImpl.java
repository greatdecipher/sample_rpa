package com.albertsons.argus.domaindb.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.data.bo.db2.custom.DB2CountCustomBO;
import com.albertsons.argus.data.repo.db2.CountDB2TablesCustomRepo;
import com.albertsons.argus.domaindb.dto.DB2DTO;
import com.albertsons.argus.domaindb.exception.DB2ServiceException;
import com.albertsons.argus.domaindb.service.DB2Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DB2ServiceImpl implements DB2Service {
    @Autowired
    private CountDB2TablesCustomRepo repo;

    @Override
    public List<DB2DTO> getDB2DTO() throws DB2ServiceException {

        try {

            List<DB2CountCustomBO> bos = new ArrayList<>();
            List<DB2DTO> dtoList = new ArrayList<>();
    
            bos = repo.getDB2DBCountCustomBOs();
    
            DB2DTO db2DTO = new DB2DTO();
    
            for(DB2CountCustomBO bo : bos){
                db2DTO = new DB2DTO();
                db2DTO.setTableName(bo.getTableNm());
                db2DTO.setRowCount(bo.getBoCount());
    
                dtoList.add(db2DTO);
            }
    
            return dtoList;

        } catch (Exception e){
            throw new DB2ServiceException("Retrieving DB2 rows failed.", e);
        }
        
    }
}