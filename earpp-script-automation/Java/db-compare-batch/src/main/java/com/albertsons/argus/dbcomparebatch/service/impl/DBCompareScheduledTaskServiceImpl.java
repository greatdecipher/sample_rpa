package com.albertsons.argus.dbcomparebatch.service.impl;

import com.albertsons.argus.dbcomparebatch.service.DBCompareScheduledTaskService;
import com.albertsons.argus.dbcomparebatch.service.DBCompareService;
import com.albertsons.argus.domaindb.dto.DB2DTO;
import com.albertsons.argus.domaindb.dto.OracleDTO;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DBCompareScheduledTaskServiceImpl implements DBCompareScheduledTaskService {
    private static final Logger LOG = LogManager.getLogger(DBCompareScheduledTaskServiceImpl.class);
    
    @Autowired
	private DBCompareService dbCompareService;

    @Override
	@Scheduled(cron = "${com.argus.dbcompare.scheduled.task.cron.value}")
	public void runDBCompareScheduleJob(){
        List<OracleDTO> oracleDTOs = new ArrayList<>();
        List<DB2DTO> db2DTOs = new ArrayList<>();
        List<String> headers = dbCompareService.getHeadersList();

        try {
            dbCompareService.getOracleAndDB2Rows(headers, oracleDTOs, db2DTOs);
        } catch (Exception e){
            LOG.log(Level.INFO, () -> "Problem performing DB Compare automation. . .");
            LOG.error(e);
        } finally {
            dbCompareService.moveLogFile();
        }
    }

}
