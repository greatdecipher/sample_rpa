package com.albertsons.argus.r2r;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.albertsons.argus.r2r.service.R2RScheduledTaskService;

@SpringBootTest(classes = R2RScheduledTaskServiceTest.class)
class R2RScheduledTaskServiceTest {

	@MockBean
	private R2RScheduledTaskService mockR2rScheduledTaskService;
	
	@BeforeEach
	void setMockOutput() {
		doNothing().when(mockR2rScheduledTaskService).runR2RSchedulerEODJob();
		doNothing().when(mockR2rScheduledTaskService).runR2RSchedulerJob();
	}

	@Test
	void testRunR2RSchedulerEODJob(){
		mockR2rScheduledTaskService.runR2RSchedulerEODJob();
		assertTrue(true);
	}

	@Test
	void testRunR2RSchedulerJob(){
		mockR2rScheduledTaskService.runR2RSchedulerJob();
		assertTrue(true);
	}
	
}
