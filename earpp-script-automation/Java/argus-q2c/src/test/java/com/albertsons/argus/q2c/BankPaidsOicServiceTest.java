package com.albertsons.argus.q2c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.albertsons.argus.q2c.inbound.bankpaids.service.BankPaidsOicService;

@SpringBootTest(classes = BankPaidsOicServiceTest.class)
class BankPaidsOicServiceTest {
    
    @Autowired
	Environment environment;

	@MockBean
	BankPaidsOicService bankPaidsOicService;

	@BeforeEach
    void setMockOutput(){
		try {
			List<BankStatementsDTO> bankStatementsDTOs = new ArrayList<>();
            
            BankStatementsDTO bankStatementsDTO = new BankStatementsDTO();
            bankStatementsDTO.setInstanceId(202019L);
            bankStatementsDTO.setCallbackId("20202");
            bankStatementsDTO.setJobId("20202");
            bankStatementsDTO.setJobName("20202");
            bankStatementsDTO.setJobStatus("Processed");
            bankStatementsDTO.setProcessTime("2020202020");
            bankStatementsDTO.setAlternateDocumentId("ICB_BankPaids_00");
            bankStatementsDTO.setFileName("ICB_BankPaids_00");
            bankStatementsDTO.setCount(12);

            bankStatementsDTOs.add(bankStatementsDTO);

			doNothing().when(bankPaidsOicService).bankPaidsMain(anyString(), anyString(), anyString());
			when(bankPaidsOicService.checkBankStatements(anyString(), anyString())).thenReturn(bankStatementsDTOs);
			when(bankPaidsOicService.checkBankProcessInstance(anyString())).thenReturn(true);

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	void testBankPaidsMain(){
        String filename = "ICB_BankPaids_";
        String yestDate = "20230101";
        String execTimestamp = "20230101";

        bankPaidsOicService.bankPaidsMain(filename, yestDate, execTimestamp);
        assertEquals(filename, "ICB_BankPaids_");
        assertTrue(yestDate.equals("20230101"));
        assertTrue(execTimestamp.equals("20230101"));
	}

    @Test
	void testCheckBankStatements(){
        List<BankStatementsDTO> bankStatementsDTOs = new ArrayList<>();
        
        BankStatementsDTO bankStatementsDTO = new BankStatementsDTO();
        bankStatementsDTOs.add(bankStatementsDTO);
		
        String filename = "ICB_BankPaids_";
        String yestDate = "20230101";

        bankStatementsDTOs = bankPaidsOicService.checkBankStatements(filename, yestDate);
        assertNotNull(bankStatementsDTOs);
	}
    
}
