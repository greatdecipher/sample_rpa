package com.albertsons.argus.domain.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.albertsons.argus.DomainApplication;
import com.albertsons.argus.domain.bo.generated.ByProjectBO;
import com.albertsons.argus.domain.service.AutomationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DomainApplication.class)
public class AutomationServiceTest {
    @Autowired
    private AutomationService<ByProjectBO> automationService;
    
    @Test
    public void automationTest()
    {
        String contentTest = "{\"items\":[{\"namespace\":\"wmq\",\"name\":\"QMPGV6671\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGV5A3\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGVD71\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGV5A4\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGV5A5\"},{\"namespace\":\"wmq\",\"name\":\"QMPGV6641\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGVD70\"},{\"namespace\":\"wmq\",\"name\":\"MQDMZPR06\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGV5A1\"},{\"namespace\":\"wmq\",\"name\":\"MQDMZPR07\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGVD6E\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGVD6F\"}],\"identifier\":\"name\",\"label\":\"name\",\"rc\":200,\"msg\":\"Data retrieved successfully\"}";
        ByProjectBO tmaProject = automationService.toJson(contentTest);

        assertEquals(true, tmaProject!=null);
        assertEquals("QMPGV6671", tmaProject.getItems().get(0).getName());
    }
}
