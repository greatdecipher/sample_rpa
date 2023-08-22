package com.albertsons.argus.r2r;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.albertsons.argus.r2r.service.R2RMimService;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@SpringBootTest(classes = R2RMimServiceTest.class)
class R2RMimServiceTest {

	@Autowired
	Environment environment;

    @MockBean
    private R2RMimService mockR2rMimService;

	@BeforeEach
    void setMockOutput(){
		List<FileDetails> files = new ArrayList<>();

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
		
		FileDetails file2 = new FileDetails();
		String filename2 = "JE_JIF_SC_20220505000000.dat";
		String filePrefix2 = "JE_JIF_SC_";
		String interfaceName2 = "GL-I-Store Charges";
		String jobName2 = "LAD13599 (LAD13599)";
		String mimInterface2 = "PRD3";
		String labelContact2 = "OracleFinancials.IT";
		String fileRequirement2 = "mandatory";
		String dataSize2 = "big";
		int kafkaConsumeTime2 = 60;
		int oicRuntime2 = 150;

		file2.setFilename(filename2);
		file2.setFilePrefix(filePrefix2);
		file2.setInterfaceName(interfaceName2);
		file2.setJobName(jobName2);
		file2.setMimInterface(mimInterface2);
		file2.setLabelContact(labelContact2);
		file2.setFileRequirement(fileRequirement2);
		file2.setDataSize(dataSize2);
		file2.setKafkaConsumeTime(kafkaConsumeTime2);
		file2.setOicRuntime(oicRuntime2);

		files.add(file1);
		files.add(file2);

		List<String> mimFilesSuccess = new ArrayList<>();
		mimFilesSuccess.add(filename1);
		mimFilesSuccess.add(filename2);

		String unsuccessType = "Source";

		try {
			when(mockR2rMimService.mainMimTask(any(), anyString(), anyString())).thenReturn(files);
			when(mockR2rMimService.getFileDetailsFromRefFile(anyString())).thenReturn(file1);
			doNothing().when(mockR2rMimService).inputLabelDetails(any(), anyString());
			doNothing().when(mockR2rMimService).navigateTransferStatusPage(any(), any());
			doNothing().when(mockR2rMimService).waitLoadMainPage(any(), anyString(), anyLong());
			when(mockR2rMimService.getResultTableValues(any(), any())).thenReturn(mimFilesSuccess);
			when(mockR2rMimService.checkUnsuccessType(any(), any(), anyString())).thenReturn(unsuccessType);
			doNothing().when(mockR2rMimService).closeUnsuccessTab(any());
			doNothing().when(mockR2rMimService).sendMimEmail(any(), any(), anyString(), anyString());

		} catch (ArgusR2RException e){
			e.printStackTrace();
		}
	}

	@Test
	void testMainMimTask(){
		FileDetails file = new FileDetails();

		try {
			mockR2rMimService.mainMimTask(file, "202208031911", "August 03, 2022 07:11:57 MST");
		
		} catch (ArgusR2RException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testGetFileDetailsFromRefFile(){
		String filePrefix = "JE_JIF_SC_";
		mockR2rMimService.getFileDetailsFromRefFile(filePrefix);
	}

	@Test
	void testInputLabelDetails(){
		Page mainPage = null;
		String labelName = "JE_JIF_SC_";
		
		try {
			mockR2rMimService.inputLabelDetails(mainPage, labelName);

			assertNull(mainPage);
			assertNotNull(labelName);

		} catch (ArgusR2RException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testNavigateTransferStatusPage(){
		Page mainPage = null;
		FileDetails fileDetails = new FileDetails();

		mockR2rMimService.navigateTransferStatusPage(mainPage, fileDetails);

		assertNull(mainPage);
		assertNotNull(fileDetails);
	}
	
	@Test
	void testWaitLoadMainPage(){
		Page mainPage = null;
		String waitElement = "Loading Processes...";
		long waitLimit = 300;

		try {
			mockR2rMimService.waitLoadMainPage(mainPage, waitElement, waitLimit);

			assertNull(mainPage);
			assertNotNull(waitElement);
			assertNotNull(waitLimit);

		} catch (ArgusR2RException e){
			e.printStackTrace();
		}
	}
	
	@Test
	void testGetResultTableValues(){
		Page mainPage = null;
		FileDetails file = new FileDetails();

		mockR2rMimService.getResultTableValues(mainPage, file);

		assertNull(mainPage);
		assertNotNull(file);
	}
	
	@Test
	void testCheckUnsuccessType(){
		Page page = null;
		ElementHandle row = null;
		String label = "JE_JIF_SC_";

		mockR2rMimService.checkUnsuccessType(page, row, label);

		assertNull(page);
		assertNull(row);
		assertEquals("JE_JIF_SC_", label);
	}
	
	@Test
	void testCloseUnsuccessTab(){
		Page page = null; 

		mockR2rMimService.closeUnsuccessTab(page);

		assertNull(page);
	}
	
	@Test
	void testSendMimEmail(){
		String[] recipients = environment.getProperty("mail.argus.edis.email", String[].class);
		String[] cc = environment.getProperty("mail.argus.cc", String[].class);
		String subject = environment.getProperty("mail.subject.mim.post.exit.failure");
		String body = "This is a test email.";

		mockR2rMimService.sendMimEmail(recipients, cc, subject, body);
	}

	@Test
	void testMainMimTask_Exception() throws ArgusR2RException {
		when(mockR2rMimService.mainMimTask(any(), anyString(), anyString())).thenThrow(new ArgusR2RException("error in MIM interface"));

		ArgusR2RException excep = assertThrows(ArgusR2RException.class, () -> {
            mockR2rMimService.mainMimTask(null, "202208031911", "August 03, 2022 07:11:57 MST");
        });

		String expectedMessage = "error in MIM interface";
        String actualMessage = excep.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testInputLabelDetails_Exception() throws ArgusR2RException {
		doThrow(new ArgusR2RException("error entering label details")).when(mockR2rMimService).inputLabelDetails(any(), anyString());

		ArgusR2RException excep = assertThrows(ArgusR2RException.class, () -> {
            mockR2rMimService.inputLabelDetails(null, "JE_JIF_SC_");
        });

		String expectedMessage = "error entering label details";
        String actualMessage = excep.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testWaitLoadMainPage_Exception() throws ArgusR2RException {
		doThrow(new ArgusR2RException("error waiting for main page to load")).when(mockR2rMimService).waitLoadMainPage(any(), anyString(), anyLong());
		
		ArgusR2RException excep = assertThrows(ArgusR2RException.class, () -> {
            mockR2rMimService.waitLoadMainPage(null, "Loading Processes...", 300);
        });

		String expectedMessage = "error waiting for main page to load";
        String actualMessage = excep.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}

}