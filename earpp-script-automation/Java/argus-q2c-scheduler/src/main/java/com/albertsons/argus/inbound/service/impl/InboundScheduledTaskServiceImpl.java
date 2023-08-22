package com.albertsons.argus.inbound.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.inbound.service.InboundScheduledTaskService;

@Service
public class InboundScheduledTaskServiceImpl implements InboundScheduledTaskService{
    private static final Logger LOG = LogManager.getLogger(InboundScheduledTaskServiceImpl.class);

    @Autowired
    Environment environment;

	@Override
	@Scheduled(cron = "${com.argus.q2c.inbound.scheduled.task.cron.value}")
	public void runQ2CInboundTaskSchedulerJob(){ // Goes through the schedule of each entry in Inbound Reference File
        LOG.log(Level.INFO, () -> "start Q2C Inbound Task script execution. . .");

        List<String> filePrefixesToRun = new ArrayList<>();

		try {
			filePrefixesToRun = getFilesToRunFromRefFile();

			if (filePrefixesToRun.size() >= 1){
				for (String filePrefix : filePrefixesToRun){
					// runs script that launches a cmd per filePrefix
					Runtime.getRuntime().exec("wscript " + environment.getProperty("run.cmd.vbscript.path") + " " + filePrefix);
				}
			}
			else{
				LOG.log(Level.INFO, () -> "no file scheduled for window time. . .");
			}

		} catch (Exception e) {
			LOG.log(Level.INFO, () -> "exception caught in Q2C Inbound Task scheduler script execution. . .");
			LOG.error(e);
		}
    
        LOG.log(Level.DEBUG, () -> "end Q2C Inbound Task script execution. . .");
    }

	private List<String> getFilesToRunFromRefFile(){
		LOG.log(Level.DEBUG, () -> "start getFilesToRunFromRefFile method . . .");

		List<String> filePrefixes = new ArrayList<>();

		try {
			AutomationUtil util = new AutomationUtil();
			Date dateNow = new Date();
			Calendar calendar = util.dateToCalendar(dateNow, environment.getProperty("q2c.timezone"));

			int minute = calendar.get(Calendar.MINUTE);

			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), minute, 0);
						
			// get all filenames falling under given window time
			File myFile = new File(environment.getProperty("q2c.inbound.reference.file")); 
			FileInputStream fis = new FileInputStream(myFile);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheet("Window Time");

			for (Row row : sheet) {
				if (row.getRowNum() != 0){ // don't count first row because it's the header
	
					for (Cell cell : row) {
						if (cell.getColumnIndex() == 8){ // MST time column
							String filePrefix = checkIfInWindowTime(row, cell, calendar);

							if (!filePrefix.equalsIgnoreCase("") || !filePrefix.isEmpty()){
								filePrefixes.add(filePrefix);
							}
						}
					}

				}
			}
	
			wb.close();

			return filePrefixes;

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "error retrieving schedule from reference file. . .");
			LOG.error(e);
			return null;
		}		

	}

	private String checkIfInWindowTime(Row row, Cell cell, Calendar calendar){
		LOG.log(Level.DEBUG, () -> "start checkIfInWindowTime method . . .");

		try {
			int numDayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
			int dayOfWeekToCheck = numDayofWeek; // SUN starts with column index 1 in reference file
			LOG.log(Level.DEBUG, () -> "day of week to check (0-based): " + dayOfWeekToCheck);

			if (cell.getCellType() == CellType.NUMERIC){
				int rowHour = cell.getLocalDateTimeCellValue().getHour();
				int rowMinute = cell.getLocalDateTimeCellValue().getMinute();
				if (rowHour == calendar.get(Calendar.HOUR_OF_DAY) && rowMinute == calendar.get(Calendar.MINUTE)){
					if (row.getCell(dayOfWeekToCheck).getStringCellValue().equalsIgnoreCase("x")){
						return row.getCell(0).getStringCellValue(); // return file prefix
					}
				}
			}

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "error checking if in window time. . .");
			LOG.error(e);
		}
		
		return "";
	}

}
