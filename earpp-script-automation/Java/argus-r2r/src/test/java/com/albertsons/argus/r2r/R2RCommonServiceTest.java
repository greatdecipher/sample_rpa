package com.albertsons.argus.r2r;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.albertsons.argus.r2r.service.R2RCommonService;

@SpringBootTest(classes = R2RCommonServiceTest.class)
class R2RCommonServiceTest {

	@Autowired
	Environment environment;

	@MockBean
	R2RCommonService mockR2rCommonService;

	@BeforeEach
    void setMockOutput(){
		try {
			String mfaCode = "100100";
			boolean isFileRunning = false;

			when(mockR2rCommonService.getMfaCode(any(), any())).thenReturn(mfaCode);
			when(mockR2rCommonService.checkIfFileIsRunning(anyString(), anyString())).thenReturn(isFileRunning);

		} catch (ArgusR2RException e){
			e.printStackTrace();
		}
	}

	@Test
	void testGetMfaCode() {
		String mfaCode = mockR2rCommonService.getMfaCode(environment.getProperty("mfa.python.script"), environment.getProperty("mfa.secret.key"));		
		assertNotNull(mfaCode);
	}

	@Test
	void testCheckIfFileIsRunning(){
		try {
			String sFile = "JE_JIF_SC_";

			boolean isFileRunning = mockR2rCommonService.checkIfFileIsRunning(environment.getProperty("file.prefixes.running"), sFile);
			assertFalse(isFileRunning);

		} catch (ArgusR2RException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testCheckIfFileIsRunning_Exception() throws ArgusR2RException {
		when(mockR2rCommonService.checkIfFileIsRunning(environment.getProperty("file.prefixes.running"), "JE_JIF_SC_")).thenThrow(new ArgusR2RException("Error checking if file is running"));

		ArgusR2RException excep = assertThrows(ArgusR2RException.class, () -> {
            mockR2rCommonService.checkIfFileIsRunning(environment.getProperty("file.prefixes.running"), "JE_JIF_SC_");
        });

		String expectedMessage = "Error checking if file is running";
        String actualMessage = excep.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}

}