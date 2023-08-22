package com.albertsons.argus.r2r;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.albertsons.argus.r2r.service.R2RErpService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

@SpringBootTest(classes = R2RErpServiceTest.class)
class R2RErpServiceTest {

	@Autowired
	Environment environment;

    @MockBean
    private R2RErpService mockR2rErpService;

	@BeforeEach
	void setMockOutput(){
		FileDetails file1 = new FileDetails();
		String filename1 = "JE_JIF_SC_20220101000000.dat";
		String filePrefix1 = "JE_JIF_SC_";
		String interfaceName1 = "GL-I-Store Charges";
		String jobName1 = "LAD13599 (LAD13599)";
		String mimInterface1 = "PRD3";
		String labelContact1 = "OracleFinancials.IT";
		String fileRequirement1 = "mandatory";
		String dataSize1 = "big";
		int kafkaConsumeTime1 = 60;
		int oicRuntime1 = 150;

		file1.setFilename(filename1);
		file1.setFilePrefix(filePrefix1);
		file1.setInterfaceName(interfaceName1);
		file1.setJobName(jobName1);
		file1.setMimInterface(mimInterface1);
		file1.setLabelContact(labelContact1);
		file1.setFileRequirement(fileRequirement1);
		file1.setDataSize(dataSize1);
		file1.setKafkaConsumeTime(kafkaConsumeTime1);
		file1.setOicRuntime(oicRuntime1);

		Page page = null;
		boolean filterFileLoaded = true;
		boolean filePosted = true;
		boolean getFilePostStatus = true;
		boolean readFilePostStatus = true;

		try {
			doNothing().when(mockR2rErpService).mainErpTask(any(), anyString(), anyString());
			when(mockR2rErpService.navigateErpLogin(any(), any())).thenReturn(page);
			when(mockR2rErpService.navigateJournals(any(), any(), any())).thenReturn(page);
			when(mockR2rErpService.checkFileLoaded(any(), anyString(), any(), any())).thenReturn(page);
			when(mockR2rErpService.filterFileLoaded(any(), any(), anyString())).thenReturn(filterFileLoaded);
			when(mockR2rErpService.checkFilePosted(any(), any(), anyString(), any(), any())).thenReturn(filePosted);
			when(mockR2rErpService.getFilePostStatus(any(), any(), any(), any(), anyString())).thenReturn(getFilePostStatus);
			when(mockR2rErpService.readFilePostStatus(any(), any())).thenReturn(readFilePostStatus);
			doNothing().when(mockR2rErpService).sleepNearestHalfHour();
			doNothing().when(mockR2rErpService).sendErpEmail(any(), any(), anyString(), anyString());

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	void testMainErpTask(){
		FileDetails fileDetails = new FileDetails();
		String groupId = "20220508201819";
		String execTimestamp = "202208031911";

		try {
			mockR2rErpService.mainErpTask(fileDetails, groupId, execTimestamp);
			assertNotNull(fileDetails);
			assertTrue(groupId.equals("20220508201819"));
			assertEquals("202208031911", execTimestamp);

		} catch (ArgusR2RException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testNavigateErpLogin(){
		Browser browser = null;
		BrowserContext browserContext = null;
		
		try {
			Page page = mockR2rErpService.navigateErpLogin(browser, browserContext);
			assertNull(page);

		} catch (ArgusR2RException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testNavigateJournals(){
		FileDetails fileDetails = new FileDetails();
		Browser browser = null;
		BrowserContext browserContext = null;

		Page page = mockR2rErpService.navigateJournals(fileDetails, browser, browserContext);
		assertNull(page);
	}

	@Test
	void testCheckFileLoaded(){
		FileDetails fileDetails = new FileDetails();
		String groupId = "20220508201819";
		Browser browser = null;
		BrowserContext browserContext = null;

		Page page = mockR2rErpService.checkFileLoaded(fileDetails, groupId, browser, browserContext);
		assertNull(page);
	}

	@Test
	void testFilterFileLoaded(){
		Page page = null;
		FileDetails fileDetails = new FileDetails();
		String groupId = "20220508201819";

		boolean filterFileLoaded = mockR2rErpService.filterFileLoaded(page, fileDetails, groupId);
		assertTrue(filterFileLoaded);
	}

	@Test
	void testCheckFilePosted(){
		Page page = null;
		FileDetails fileDetails = new FileDetails();
		String groupId = "20220508201819";
		Browser browser = null;
		BrowserContext browserContext = null;

		boolean checkFilePosted = mockR2rErpService.checkFilePosted(page, fileDetails, groupId, browser, browserContext);
		assertTrue(checkFilePosted);
	}

	@Test
	void testGetFilePostStatus(){
		Browser browser = null;
		BrowserContext browserContext = null;
		Page page = null;
		FileDetails fileDetails = new FileDetails();
		String groupId = "20220508201819";

		boolean getFilePostStatus = mockR2rErpService.getFilePostStatus(page, fileDetails, browser, browserContext, groupId);
		assertTrue(getFilePostStatus);
	}
	
	@Test
	void testReadFilePostStatus(){	
		Page page = null;
		FileDetails fileDetails = new FileDetails();

		boolean readFilePostStatus = mockR2rErpService.readFilePostStatus(page, fileDetails);
		assertTrue(readFilePostStatus);
	}

	@Test
	void testSleepNearestHalfHour(){
		mockR2rErpService.sleepNearestHalfHour();
	}

	@Test
	void testSendErpEmail(){
		String[] recipients = environment.getProperty("mail.argus.recipients", String[].class);
		String[] cc = environment.getProperty("mail.argus.cc", String[].class);
		String subject = environment.getProperty("mail.subject.erp.success");
		String body = "This is a test email.";

		mockR2rErpService.sendErpEmail(recipients, cc, subject, body);
	}

	@Test
	void testMainErpTask_Exception() throws ArgusR2RException {
		doThrow(new ArgusR2RException("error in ESS Jobs interface")).when(mockR2rErpService).mainErpTask(any(), anyString(), anyString());

		FileDetails fileDetails = new FileDetails();

		ArgusR2RException excep = assertThrows(ArgusR2RException.class, () -> {
            mockR2rErpService.mainErpTask(fileDetails, "20220508201819", "202208031911");
        });

		String expectedMessage = "error in ESS Jobs interface";
        String actualMessage = excep.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}

	
	@Test
	void testNavigateErpLogin_Exception() throws ArgusR2RException {
		when(mockR2rErpService.navigateErpLogin(any(), any())).thenThrow(new ArgusR2RException("problem navigating ERP login"));

		ArgusR2RException excep = assertThrows(ArgusR2RException.class, () -> {
           mockR2rErpService.navigateErpLogin(null, null);
        });

		String expectedMessage = "problem navigating ERP login";
        String actualMessage = excep.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}

}