package com.albertsons.argus.hire.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.albertsons.argus.ArgusHireApplication;
import com.albertsons.argus.hire.exception.HireException;
import com.albertsons.argus.hire.exception.HirePlaywrightException;
import com.albertsons.argus.hire.service.HirePlaywrightService;
import com.albertsons.argus.hire.service.HireService;

@SpringBootTest(classes = ArgusHireApplication.class)
public class HireServiceTest {
    @Autowired
    private HireService hireService;

    @MockBean
    private HirePlaywrightService hirePlaywrightServiceMock;

    @Test
    public void hireRehireDateTest(){
        String hireStr = "";
        try {
            doNothing().when(hirePlaywrightServiceMock).browseStraightProcessing(anyString(), anyString(), anyString(), anyString(), anyString());
        } catch (HirePlaywrightException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            hireStr = hireService.hireRehireDate("Straight Through Processing", "434", "343245", "4324", "3/31/23");
        } catch (HireException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        assertEquals(hireStr, "Scenario_1");
    }
}
