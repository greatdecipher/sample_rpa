package com.albertsons.argus.q2c;

import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import com.albertsons.argus.inbound.service.InboundScheduledTaskService;

@SpringBootTest(classes = InboundScheduledTaskServiceTest.class)
public class InboundScheduledTaskServiceTest {
    
    @Autowired
	Environment environment;

	@MockBean
	InboundScheduledTaskService inboundScheduledTaskService;

    @BeforeEach
    void setMockOutput(){
		try {
            doNothing().when(inboundScheduledTaskService).runQ2CInboundTaskSchedulerJob();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
	void testRunQ2CInboundTaskSchedulerJob(){
        inboundScheduledTaskService.runQ2CInboundTaskSchedulerJob();
	}

}
