package com.albertsons.argus.r2r;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.albertsons.argus.r2r.service.R2RSchedulerService;

@SpringBootTest(classes = R2RSchedulerServiceTest.class)
class R2RSchedulerServiceTest {

	@MockBean
	private R2RSchedulerService mockR2rSchedulerService;
	
	@BeforeEach
	void setMockOutput() {
		List<String> filesToRun = new ArrayList<>();
		String filePrefix1 = "JE_JIF_SC";
		String filePrefix2 = "JE_GTR_NH_PY";

		filesToRun.add(filePrefix1);
		filesToRun.add(filePrefix2);
		
		when(mockR2rSchedulerService.getFilesToRunFromRefFile()).thenReturn(filesToRun);
	}

	@Test
	void testGetFilesToRunFromRefFile(){
		List<String> filesToRun = mockR2rSchedulerService.getFilesToRunFromRefFile();
		assertNotNull(filesToRun);
	}
	
}
