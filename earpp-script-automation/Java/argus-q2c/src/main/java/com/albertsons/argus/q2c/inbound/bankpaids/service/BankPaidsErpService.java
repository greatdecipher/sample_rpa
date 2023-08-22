package com.albertsons.argus.q2c.inbound.bankpaids.service;

import java.util.List;

import com.albertsons.argus.domaindbq2c.dto.BankStatementsDTO;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

public interface BankPaidsErpService {
    public void bankPaidsErpReadBankMain(String yestDate, List<BankStatementsDTO> bankStatementsDTOs, String execTimestamp);
    public List<ElementHandle> searchAutoReconcileBankStatements(Page page);
    public boolean getArgument1(Page page, List<ElementHandle> searchResults, int firstVisitFlag);
    public void bankPaidsErpMain(List<BankStatementsDTO> bankStatementsDTOs, String execTimestamp);
    public boolean searchIdInScheduledProcesses(Page page, String jobId);
    public String getAttachmentContent(Page page, String filename, String textToLookFor, String altDocId);
    public String readAttachment(Page page, String filePath, String filename, String textToLookFor, String altDocId);
}
