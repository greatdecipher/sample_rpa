package com.albertsons.argus.tma.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.TmaArgusTestApplication;
import com.albertsons.argus.domain.bo.generated.ByProjectBO;
import com.albertsons.argus.domain.bo.generated.ByQueueBO;
import com.albertsons.argus.domain.bo.generated.TmaItem;
import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.tma.exception.ArgusTmaException;
import com.albertsons.argus.tma.service.TMAAutomationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { TmaArgusTestApplication.class })
class TMAAutomationServiceTest {

    @Autowired
    private TMAAutomationService tmaAutomationService;

    @Autowired
    private AutomationService<ByProjectBO> automationService;

    @MockBean
    private EmailService emailMock;

    @BeforeEach
    void beforeLoad(){
        try {
            doNothing().when(emailMock).sendSimpleMessage(anyString(), anyString(), any(String[].class), any(String[].class), anyString(), anyString(), 
            anyInt(),anyBoolean());
        } catch (ArgusMailException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getQMManagersTest() {
        String contentTest = "{\"items\":[{\"namespace\":\"wmq\",\"name\":\"QMPGV6671\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGV5A3\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGVD71\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGV5A4\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGV5A5\"},{\"namespace\":\"wmq\",\"name\":\"QMPGV6641\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGVD70\"},{\"namespace\":\"wmq\",\"name\":\"MQDMZPR06\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGV5A1\"},{\"namespace\":\"wmq\",\"name\":\"MQDMZPR07\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGVD6E\"},{\"namespace\":\"wmq\",\"name\":\"IIBPGVD6F\"}],\"identifier\":\"name\",\"label\":\"name\",\"rc\":200,\"msg\":\"Data retrieved successfully\"}";
        ByProjectBO tmaProject = automationService.toJson(contentTest);
        List<String> valLists = tmaAutomationService.getQMManagers(tmaProject);

        assertEquals(true, valLists.size() > 0);
        assertEquals(8, valLists.size());
    }

    @Test
    @Disabled("Since it was thrown by the playwright. Should not connect to the host")
    void whenArgusTmaExceptionThrown_thenAssertionSucceds() {
        Exception exception = assertThrows(ArgusTmaException.class, () -> {
            tmaAutomationService.getbyProjectName(null);
        });

        String expectedMessage = "The text content is empty.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getHeadersListTest() {
        List<String> lists = tmaAutomationService.getHeadersList();

        assertEquals(true, !lists.isEmpty());
        assertEquals(true, lists.size() > 0);
    }

    @Test
    void sendTmaEmailTest() {
        //Please set to 50 from current queue deplth(argus-app.properties) in order to work these scenario

        List<ByQueueBO> bos = new ArrayList<>();
        List<TmaItem> items = new ArrayList<>();
        
        List<String> headers = new ArrayList<>();
        ByQueueBO byQueueBO = null;
        TmaItem tmaItem = null;
        headers.add("WMQ Connection Name");
        headers.add("Queue Name");
        headers.add("Current Queue Depth");
        headers.add("Max Depth");
        headers.add("Readers");
        headers.add("Writers");

        byQueueBO = new ByQueueBO();
        tmaItem = new TmaItem();
        tmaItem.setCommandLevel(1);
        tmaItem.setCurrentQDepth(51);
        tmaItem.setDlq(false);
        tmaItem.setId("22");
        tmaItem.setMaxQDepth(50000);
        tmaItem.setOpenInputCount(2);
        tmaItem.setOpenOutputCount(5);
        tmaItem.setPlatform(0);
        tmaItem.setQName("qName");
        tmaItem.setQType(1);
        tmaItem.setUsage(2);
        tmaItem.setWMQConnection("Con");

        items.add(tmaItem);

        byQueueBO.setItems(items);
        bos.add(byQueueBO);

        tmaAutomationService.sendTmaEmail(headers, bos);

        assertEquals(true, bos!=null);
        assertEquals(true, bos.size()>0);
        //Scenrio 2 - when the queue depth is not met
        List<TmaItem> items2 = new ArrayList<>();
        List<ByQueueBO> bos2 = new ArrayList<>();

        byQueueBO = new ByQueueBO();
        tmaItem = new TmaItem();
        tmaItem.setCommandLevel(11);
        tmaItem.setCurrentQDepth(10);
        tmaItem.setDlq(false);
        tmaItem.setId("23");
        tmaItem.setMaxQDepth(50000);
        tmaItem.setOpenInputCount(5);
        tmaItem.setOpenOutputCount(10);
        tmaItem.setPlatform(0);
        tmaItem.setQName("qName");
        tmaItem.setQType(11);
        tmaItem.setUsage(22);
        tmaItem.setWMQConnection("Con");

        items2.add(tmaItem);

        byQueueBO.setItems(items2);
        bos2.add(byQueueBO);

        tmaAutomationService.sendTmaEmail(headers, bos2);

        assertEquals(true, bos2!=null);
        assertEquals(true, bos2.size()>0);
        
    }

}
