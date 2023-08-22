package com.albertsons.argus.r2r.service.impl;

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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.r2r.service.R2RSchedulerService;

@Service
public class R2RSchedulerServiceImpl implements R2RSchedulerService {
	public static final Logger LOG = LogManager.getLogger(R2RSchedulerServiceImpl.class);

	@Autowired
    private Environment environment;

	@Override
	public List<String> getFilesToRunFromRefFile(){
		LOG.log(Level.DEBUG, () -> "start getFilesToRunFromRefFile method . . .");

		List<String> filePrefixes = new ArrayList<>();

		try {
			AutomationUtil util = new AutomationUtil();
			Date dateNow = new Date();
			Calendar calendar = util.dateToCalendar(dateNow, environment.getProperty("r2r.timezone"));

			// calculate what minute bot will check for (either n:00:00 or n:30:00)
			int minuteNow = calendar.get(Calendar.MINUTE);
			int subtractNum = minuteNow % 30;
			int roundDownMinute = minuteNow - subtractNum;

			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), roundDownMinute, 0);
						
			// get all filenames falling under given window time
			File myFile = new File(environment.getProperty("r2r.reference.file")); 
			FileInputStream fis = new FileInputStream(myFile);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheet("Window Time");

			for (Row row : sheet) {
				if (row.getRowNum() != 0){ // don't count first row because it's the header
	
					for (Cell cell : row) {
						if (cell.getColumnIndex() == 9){ // MST time column
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
			e.printStackTrace();
			return null;
		}		

	}

	private String checkIfInWindowTime(Row row, Cell cell, Calendar calendar){
		LOG.log(Level.DEBUG, () -> "start checkIfInWindowTime method . . .");

		try {
			int numDayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
			int dayOfWeekToCheck = numDayofWeek + 1; // SUN starts with column index 2 in reference file
			LOG.log(Level.DEBUG, () -> "day of week to check (0-based): " + dayOfWeekToCheck);

			if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().toLowerCase().equalsIgnoreCase("anytime")){
				if (row.getCell(dayOfWeekToCheck).getStringCellValue().equalsIgnoreCase("x")){
					return row.getCell(1).getStringCellValue(); // return file prefix
				}
			}
			else if (cell.getCellType() == CellType.NUMERIC){
				int rowHour = cell.getLocalDateTimeCellValue().getHour();
				int rowMinute = cell.getLocalDateTimeCellValue().getMinute();
	
				if (rowHour == calendar.get(Calendar.HOUR_OF_DAY) && rowMinute == calendar.get(Calendar.MINUTE)){
					if (row.getCell(dayOfWeekToCheck).getStringCellValue().equalsIgnoreCase("x")){
						return row.getCell(1).getStringCellValue(); // return file prefix
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
