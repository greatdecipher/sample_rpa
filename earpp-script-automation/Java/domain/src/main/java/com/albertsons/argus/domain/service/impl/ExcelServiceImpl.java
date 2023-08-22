package com.albertsons.argus.domain.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.exception.DomainException;
import com.albertsons.argus.domain.service.ExcelService;
import com.albertsons.argus.domain.util.AutomationUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ExcelServiceImpl implements ExcelService{

    @Override
    public Map<Integer, List<String>> getExcelContentByRowMap(String directoryPath, String fileName) throws DomainException{
        AutomationUtil automationUtil = new AutomationUtil();
        Map<Integer, List<String>> data = new HashMap<>();
        FileInputStream fis = null;
        
        String fileInomingStr = directoryPath+fileName;

        log.info("File Director: "+fileInomingStr);
        
        if(automationUtil.fileExists(fileInomingStr)){
            File file = new File(fileInomingStr);
                
                try {
                    fis = automationUtil.getFileInputStream(file);
                } catch (FileNotFoundException e) {
                   log.error("getExcelContentByRowMap exception: "+e.getMessage());
                   throw new DomainException("getExcelContentByRowMap exception: "+e.getMessage());
                }
        
                try (Workbook workbook = new XSSFWorkbook(fis)) {
                    Sheet sheet = workbook.getSheetAt(0);
                    int i = 0;
                    DecimalFormat decimalFormat = new DecimalFormat("#");

                    for (Row row : sheet) {
                        data.put(i, new ArrayList<String>());
                        for (Cell cell : row) {
                            switch (cell.getCellType()) {
                                case STRING:  
                                data.get(Integer.valueOf(i)).add(cell.getRichStringCellValue().getString());
                                break;
                                case NUMERIC: 
                                if(DateUtil.isCellDateFormatted(cell)){
                                    String dateString = automationUtil.toDateString(cell.getDateCellValue(), "M/dd/yyyy", "Asia/Singapore");
                                    data.get(Integer.valueOf(i)).add(dateString);
                                }else{
                                    data.get(Integer.valueOf(i)).add(decimalFormat.format(cell.getNumericCellValue()));
                                }
                                break;
                                case BOOLEAN: 
        
                                 break;
                                case FORMULA: 
                                
                                break;
                                default: data.get(Integer.valueOf(i)).add(" ");
                            }
                        }
                        i++;
                    }
        
                } catch (IOException e) {
                    log.error("getExcelContentByRowMap exception: "+e.getMessage());
                    throw new DomainException("getExcelContentByRowMap exception: "+e.getMessage());
                }
        }else{
            log.error("Excel File not found. Please checked.");
            throw new DomainException("Excel File not found. Please checked.");
        }

        return data;
    }
    
}
