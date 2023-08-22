package com.albertsons.argus.domain.service;

import java.util.List;
import java.util.Map;

import com.albertsons.argus.domain.exception.DomainException;

public interface ExcelService {
    public Map<Integer, List<String>> getExcelContentByRowMap(String directoryPath, String fileName) throws DomainException;
}
