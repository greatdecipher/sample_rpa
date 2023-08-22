package com.albertsons.argus.domain.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import com.albertsons.argus.DomainApplication;
import com.albertsons.argus.domain.dto.QueueDTO;
import com.albertsons.argus.domain.util.AutomationUtil;

@SpringBootTest(classes = DomainApplication.class)
public class ArgusAutomationUtilTest {
    private static final Logger LOG = LogManager.getLogger(ArgusAutomationUtilTest.class);

    @Autowired
    private Environment env;

    @Test
    public void toTmaHtmlStringTestSetTo50() {
        AutomationUtil automationUtil = new AutomationUtil();
        QueueDTO itemBO = null;
        List<QueueDTO> tmaBOlists = new ArrayList<>();

        // Mock Data
        itemBO = new QueueDTO();
        // 1
        itemBO.setCurrentQueueDepth(51);
        itemBO.setMaxQueueDepth(100);
        itemBO.setReader(1);
        itemBO.setWriter(2);
        itemBO.setQueueName("qName");
        itemBO.setConnection("wMQConnection");

        tmaBOlists.add(itemBO);

        // 2
        itemBO = new QueueDTO();

        itemBO.setCurrentQueueDepth(100);
        itemBO.setMaxQueueDepth(150);
        itemBO.setReader(3);
        itemBO.setWriter(1);
        itemBO.setQueueName("qName 2");
        itemBO.setConnection("wMQConnection 2");

        tmaBOlists.add(itemBO);

        List<String> headers = new ArrayList<>();

        headers.add("WMQ Connection Name");
        headers.add("Queue Name");
        headers.add("Current Queue Depth");
        headers.add("Max Depth");
        headers.add("Readers");
        headers.add("Writers");

        String actualVal = automationUtil.toTmaHtmlString(headers, tmaBOlists, 50, true);
        LOG.log(Level.INFO, () -> "tmaBOlists size: " + tmaBOlists.size());
        LOG.log(Level.INFO, () -> "actualVal: " + actualVal);

        assertEquals(true, actualVal.length() > 0);

    }

    @Test
    public void testDate() {
        AutomationUtil automationUtil = new AutomationUtil();

        String actualVal = automationUtil.toDateString(new Date(), env.getProperty("domain.util.date.format"), "");

        assertEquals(true, actualVal.length() > 0);
    }

    @Test
    public void testgetStringTokenLists(){
        AutomationUtil automationUtil = new AutomationUtil();
        String str1 = "IMT-SWE | SWE_2390 | Meraki ARP Request | 721138890334881987";
        List<String> strLists = automationUtil.getStringTokenLists(str1, "|");
        List<String> strLists2 = automationUtil.getStringTokenLists("IMT-SWE | SWE_2390 | Meraki ARP Request | 721138890334881987~IMT-SWE | SWE_2390 | Meraki ARP Request | 721138890334881987", "~");
        List<String> strLists3 = automationUtil.getStringTokenLists(str1, "~");
        
        assertEquals(true, !strLists.isEmpty());
        assertEquals(4, strLists.size());

        assertEquals(true, !strLists2.isEmpty());
        assertEquals(2, strLists2.size());

        assertEquals(1, strLists3.size());

    }

    @Test
	void testCreateFile(){
        AutomationUtil automationUtil = new AutomationUtil();
		String strMessage = "This file prefix is currently running";
		String sFile = "JE_JIF_SC_";

		automationUtil.createFile(strMessage, sFile);

		assertNotNull(strMessage);
		assertNotNull(sFile);
	}

	@Test
	void testDeleteFile(){
        AutomationUtil automationUtil = new AutomationUtil();
        String folder = "C:\\Automations\\TestFolder";
		int maxAge = 1;
		String filename = "testFile";
		boolean deleteByAge = true;

		automationUtil.deleteFile(folder, maxAge, filename, deleteByAge);

        assertEquals(true, folder.length() > 0);
	}

	@Test
	void testFileExists(){
        AutomationUtil automationUtil = new AutomationUtil();
        String folder = "C:\\Automations\\TestFolder\\";
		String filename = "testFile";
		boolean fileExists = automationUtil.fileExists(folder + filename);
		assertFalse(fileExists);
	}

    @Test
    @Disabled
	void getAllFileNameInDirectoryTest() throws SecurityException, IOException{
        AutomationUtil automationUtil = new AutomationUtil();
        String directory = "C:\\Automations\\hire\\ss";

		List<String> fileExists = automationUtil.getAllFileNameInDirectory(directory);
        // fileExists.stream().forEach(System.out::println);

		assertTrue(!fileExists.isEmpty());
	}

    @Test
	void deleteAllFileInFolderTest() throws SecurityException, IOException{
        AutomationUtil automationUtil = new AutomationUtil();
        String directory = "C:\\Automations\\hire\\ss";

		automationUtil.deleteAllFileInFolder(directory);
        // fileExists.stream().forEach(System.out::println);

		assertTrue(true);
	}
    
   
}
