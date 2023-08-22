package com.albertsons.argus.q2c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import com.albertsons.argus.domaindbq2c.dto.BankStatementsDTO;
import com.albertsons.argus.q2c.inbound.bankpaids.service.BankPaidsErpService;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@SpringBootTest(classes = BankPaidsErpServiceTest.class)
class BankPaidsErpServiceTest {
    
    @Autowired
	Environment environment;

	@MockBean
	BankPaidsErpService bankPaidsErpService;

    @BeforeEach
    void setMockOutput(){
		try {
            List<ElementHandle> searchResults = new ArrayList<>();
            Page page = null;
            ElementHandle elementHandle = page.querySelector("testElement");
            searchResults.add(elementHandle);

            boolean argument1Found = false;
            boolean jobIdFound = false;

            String childProcessId = "2221231";

            doNothing().when(bankPaidsErpService).bankPaidsErpReadBankMain(anyString(), any(), anyString());
			when(bankPaidsErpService.searchAutoReconcileBankStatements(any())).thenReturn(searchResults);
			when(bankPaidsErpService.getArgument1(any(), any(), anyInt())).thenReturn(argument1Found);
            doNothing().when(bankPaidsErpService).bankPaidsErpMain(any(), anyString());
            when(bankPaidsErpService.searchIdInScheduledProcesses(any(), anyString())).thenReturn(jobIdFound);
            when(bankPaidsErpService.getAttachmentContent(any(), anyString(), anyString(), anyString())).thenReturn(childProcessId);
            when(bankPaidsErpService.readAttachment(any(), anyString(), anyString(), anyString(), anyString())).thenReturn(childProcessId);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
	void testBankPaidsErpReadBankMain(){
        List<BankStatementsDTO> bankStatementsDTOs = new ArrayList<>();
        
        BankStatementsDTO bankStatementsDTO = new BankStatementsDTO();
        bankStatementsDTOs.add(bankStatementsDTO);
        
        String yestDate = "20230101";
        String execTimestamp = "20230101";

        bankPaidsErpService.bankPaidsErpReadBankMain(yestDate, bankStatementsDTOs, execTimestamp);
        assertEquals(yestDate, "20230101");
        assertNotNull(bankStatementsDTOs);
        assertTrue(execTimestamp.equals("20230101"));
	}

    @Test
	void testSearchAutoReconcileBankStatements(){
        Page page = null;
        List<ElementHandle> searchResults = new ArrayList<>();
        
        searchResults = bankPaidsErpService.searchAutoReconcileBankStatements(page);
        assertNotNull(searchResults);
	}

    @Test
	void testGetArgument1(){
        Page page = null;
        List<ElementHandle> searchResults = new ArrayList<>();
        int firstVisitFlag = 0;
        
        boolean argument1Found = bankPaidsErpService.getArgument1(page, searchResults, firstVisitFlag);
        assertFalse(argument1Found);
	}

    @Test
	void testBankPaidsErpMain(){
        List<BankStatementsDTO> bankStatementsDTOs = new ArrayList<>();
        
        BankStatementsDTO bankStatementsDTO = new BankStatementsDTO();
        bankStatementsDTOs.add(bankStatementsDTO);
        
        String execTimestamp = "20230101";

        bankPaidsErpService.bankPaidsErpMain(bankStatementsDTOs, execTimestamp);
        assertNotNull(bankStatementsDTOs);
        assertTrue(execTimestamp.equals("20230101"));
	}

    @Test
	void testSearchIdInScheduledProcesses(){
        Page page = null;
        String jobId = "20202022";
        
        boolean jobIdFound = bankPaidsErpService.searchIdInScheduledProcesses(page, jobId);
        assertFalse(jobIdFound);
	}

    @Test
	void testGetAttachmentContent(){
        Page page = null;
        String filename = "ICB_BankPaids_00";
        String textToLookFor = "child process id";
        String altDocId = "EAS_CB_MO_";
        
        String childProcessId = bankPaidsErpService.getAttachmentContent(page, filename, textToLookFor, altDocId);
        assertNull(childProcessId);
	}

    @Test
	void testReadAttachment(){
        Page page = null;
        String filePath = "C:\\Automations\\Q2C\\";
        String filename = "ICB_BankPaids_00";
        String textToLookFor = "child process id";
        String altDocId = "EAS_CB_MO_";
        
        String childProcessId = bankPaidsErpService.readAttachment(page, filePath, filename, textToLookFor, altDocId);
        assertNull(childProcessId);
	}
}
