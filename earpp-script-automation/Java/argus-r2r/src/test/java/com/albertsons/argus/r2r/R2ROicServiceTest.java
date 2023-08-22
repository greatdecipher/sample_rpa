package com.albertsons.argus.r2r;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.albertsons.argus.domaindbr2r.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbr2r.dto.RecordDTO;
import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.albertsons.argus.r2r.service.R2ROicService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

@SpringBootTest(classes = R2ROicServiceTest.class)
class R2ROicServiceTest {

	@Autowired
	Environment environment;

    @MockBean
    private R2ROicService mockR2rOicService;

	@BeforeEach
	void setMockOutput(){
		List<Object> returnOicObjects = new ArrayList<>();

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

		RecordDTO recordDTO = new RecordDTO();
		Long groupId = 20220508201819L;
		String batchName = "JE_JIF_SC_20220101000000.dat";
		Long totalRec = 20799L;
		Long consumedRec = 20799L;
		Long remainingRec = 0L;
		String sumEnteredDr = null;
		String sumEnteredCr = null;
		String minProcessTime = null;
		String maxProcessTime = null;

		recordDTO.setGroup_Id(groupId);
		recordDTO.setBatch_Name(batchName);
		recordDTO.setTotal_Rec(totalRec);
		recordDTO.setConsumed_Rec(consumedRec);
		recordDTO.setRemaining_Rec(remainingRec);
		recordDTO.setSum_Entered_Dr(sumEnteredDr);
		recordDTO.setSum_Entered_Cr(sumEnteredCr);
		recordDTO.setMin_Process_Time(minProcessTime);
		recordDTO.setMax_Process_Time(maxProcessTime);

		returnOicObjects.add(file1);
		returnOicObjects.add(recordDTO);

		ProcessInstanceDTO piDTO = new ProcessInstanceDTO();
		piDTO = new ProcessInstanceDTO();
		piDTO.setInstance_Id(90000000L);
		piDTO.setIntegration_Name("Journal Entries to Oracle Cloud GL");
		piDTO.setIntegration_Pattern("Inbound");
		piDTO.setRun_Date(null);
		piDTO.setStart_Time(null);
		piDTO.setEnd_Time(null);
		piDTO.setStatus_Time(null);
		piDTO.setAttribute1(null);
		piDTO.setAttribute2(null);
		piDTO.setAttribute3(null);
		piDTO.setAttribute4(null);
		piDTO.setAttribute5(null);
		piDTO.setAttribute6(null);
		piDTO.setAttribute7(null);
		piDTO.setAttribute8(null);
		piDTO.setAttribute9(null);
		piDTO.setAttribute10(null);
		piDTO.setAttribute11(null);
		piDTO.setAttribute12(null);
		piDTO.setAttribute13(null);
		piDTO.setAttribute14(null);
		piDTO.setAttribute15(null);
		piDTO.setStatus(null);
		piDTO.setAic_Instance_Id(null);
		piDTO.setTransformation_Status("TRANSFORMED_ERP");
		piDTO.setStart_Batch(null);
		piDTO.setEnd_Batch(null);
		piDTO.setInterface_Id("INT46");
		piDTO.setTransformation_Msg(null);
		piDTO.setBatch_Name("JE_JIF_SC_20220101000000.dat");
		piDTO.setParent_Instance_Id(null);
		piDTO.setProcess_Time(null);

		boolean checkTransformStatus = true;
		Page oicLogin = null;
		String oicUiStatus = "Succeeded";
		String updateQuery = "update fusionintegration.abs_process_instance_tbl";
				updateQuery += "set process_time = '2022-05-21 07:38:33.633299'";
				updateQuery += " from fusionintegration.abs_process_instance_tbl where interface_id = 'INT46'";
				updateQuery += " and batch_name = 'JE_JIF_SC_20220101000000.dat' and instance_id = '80000000'";
				updateQuery += " and transformation_status = 'TRANSFORMED_NO_VAL' and process_time = '2022-05-21 13:38:33.633299'";

		try {
			when(mockR2rOicService.mainOicTask(any(), anyString())).thenReturn(returnOicObjects);
			when(mockR2rOicService.checkFileReceived(any())).thenReturn(filename1);
			when(mockR2rOicService.checkFileConsumed(any(), anyInt())).thenReturn(recordDTO);
			when(mockR2rOicService.queryTransformStatus(any())).thenReturn(piDTO);
			when(mockR2rOicService.checkTransformStatus(any())).thenReturn(checkTransformStatus);
			when(mockR2rOicService.checkOicUi(any(), any())).thenReturn(oicUiStatus);
			when(mockR2rOicService.navigateOicLogin(any(), any())).thenReturn(oicLogin);
			when(mockR2rOicService.checkTransformStatusOIC(any(), any(), any())).thenReturn(oicUiStatus);
			when(mockR2rOicService.generateUpdateQuery(any(), any())).thenReturn(updateQuery);
			doNothing().when(mockR2rOicService).sendOicEmail(any(), any(), anyString(), anyString());

		} catch (Exception e){
			e.printStackTrace();
		}

	}

	@Test
	void testMainOicTask(){
		List<Object> returnOicOjects = new ArrayList<>();
		FileDetails fileDetails = new FileDetails();
		String execTimestamp = "202208031911";

		try {
			returnOicOjects = mockR2rOicService.mainOicTask(fileDetails, execTimestamp);
			assertNotNull(returnOicOjects);

		} catch (ArgusR2RException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testCheckFileReceived(){
		FileDetails fileDetails = new FileDetails();

		String filename = mockR2rOicService.checkFileReceived(fileDetails);
		assertNotNull(filename);
	}

	@Test
	void testCheckFileConsumed(){
		FileDetails fileDetails = new FileDetails();
		int greaterConsumedFlag = 0;

		RecordDTO recordDTO = mockR2rOicService.checkFileConsumed(fileDetails, greaterConsumedFlag);
		assertNotNull(recordDTO);
	}

	@Test
	void testQueryTransformStatus(){
		FileDetails fileDetails = new FileDetails();

		ProcessInstanceDTO piDTO = mockR2rOicService.queryTransformStatus(fileDetails);
		assertNotNull(piDTO);
	}

	@Test
	void testCheckTransformStatus(){
		FileDetails fileDetails = new FileDetails();

		boolean result = mockR2rOicService.checkTransformStatus(fileDetails);
		assertTrue(result);
	}

	@Test
	void testCheckOicUi(){
		FileDetails fileDetails = new FileDetails();
		ProcessInstanceDTO piDTO = new ProcessInstanceDTO();

		String oicUIStatus = mockR2rOicService.checkOicUi(fileDetails, piDTO);
		assertEquals("Succeeded", oicUIStatus);
	}

	@Test
	void testNavigateOicLogin(){
		Browser browser = null;
		BrowserContext browserContext = null;
		
		Page page = mockR2rOicService.navigateOicLogin(browser, browserContext);
		assertNull(page);
	}

	@Test
	void testCheckTransformStatusOIC(){
		FileDetails fileDetails = new FileDetails();
		Page page = null;
		ProcessInstanceDTO piDTO = new ProcessInstanceDTO();

		String oicUIStatus = mockR2rOicService.checkTransformStatusOIC(fileDetails, page, piDTO);
		assertNotNull(oicUIStatus);
	}

	@Test
	void testGenerateUpdateQuery(){
		FileDetails fileDetails = new FileDetails();
		ProcessInstanceDTO piDTO = new ProcessInstanceDTO();

		String updateQuery = mockR2rOicService.generateUpdateQuery(fileDetails, piDTO);
		assertNotNull(updateQuery);
	}

	@Test
	void testSendOicEmail(){
		String[] recipients = environment.getProperty("mail.argus.recipients", String[].class);
		String[] cc = environment.getProperty("mail.argus.cc", String[].class);
		String subject = environment.getProperty("mail.subject.oic.success");
		String body = "This is a test email.";

		mockR2rOicService.sendOicEmail(recipients, cc, subject, body);
	}

	@Test
	void testMainOicTask_Exception() throws ArgusR2RException {
		when(mockR2rOicService.mainOicTask(any(), anyString())).thenThrow(new ArgusR2RException("error in OIC interface"));

		ArgusR2RException excep = assertThrows(ArgusR2RException.class, () -> {
            mockR2rOicService.mainOicTask(null, "202208031911");
        });

		String expectedMessage = "error in OIC interface";
        String actualMessage = excep.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}

}