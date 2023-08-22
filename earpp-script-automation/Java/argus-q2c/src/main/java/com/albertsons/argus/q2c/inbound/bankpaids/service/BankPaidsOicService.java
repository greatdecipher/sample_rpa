package com.albertsons.argus.q2c.inbound.bankpaids.service;

import java.util.List;

import com.albertsons.argus.domaindbq2c.dto.BankStatementsDTO;

public interface BankPaidsOicService {
    public static final Integer NORMAL_PRIORITY = 3;

    public void bankPaidsMain(String filename, String yestDate, String execTimestamp);
    public List<BankStatementsDTO> checkBankStatements(String filename, String yestDate);
    public boolean checkBankProcessInstance(String filename);
}
