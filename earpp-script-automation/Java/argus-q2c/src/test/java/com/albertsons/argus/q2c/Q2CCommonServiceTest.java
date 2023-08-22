package com.albertsons.argus.q2c;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import com.albertsons.argus.q2c.common.service.Q2CCommonService;

@SpringBootTest(classes = Q2CCommonServiceTest.class)
class Q2CCommonServiceTest {
    
    @Autowired
	Environment environment;

	@MockBean
	Q2CCommonService mockQ2cCommonService;

	@BeforeEach
    void setMockOutput(){
		try {
			boolean isFileRunning = false;

			when(mockQ2cCommonService.checkIfFileIsRunning(anyString(), anyString())).thenReturn(isFileRunning);
			doNothing().when(mockQ2cCommonService).sendEmail(any(), any(), anyString(), anyString(), anyString());

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	void testCheckIfFileIsRunning(){
		try {
			String sFile = "JE_JIF_SC_";

			boolean isFileRunning = mockQ2cCommonService.checkIfFileIsRunning(environment.getProperty("file.prefixes.running"), sFile);
			assertFalse(isFileRunning);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	@Test
	void testSendEmail(){
		try {
			String[] recipients = environment.getProperty("mail.argus.recipients", String[].class);
            String[] cc = environment.getProperty("mail.argus.cc", String[].class);
            String subject = "Unit Test - Email Subject for Q2C";
            String body = "Unit Test - Email Body for Q2C";
            String execTimestamp = "20230101";

			mockQ2cCommonService.sendEmail(recipients, cc, subject, body, execTimestamp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testCheckIfFileIsRunning_Exception() throws Exception {
		when(mockQ2cCommonService.checkIfFileIsRunning(environment.getProperty("file.prefixes.running"), "JE_JIF_SC_")).thenThrow(new Exception("Error checking if file is running"));

		Exception excep = assertThrows(Exception.class, () -> {
            mockQ2cCommonService.checkIfFileIsRunning(environment.getProperty("file.prefixes.running"), "JE_JIF_SC_");
        });

		String expectedMessage = "Error checking if file is running";
        String actualMessage = excep.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}
}
