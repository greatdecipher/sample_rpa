package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.albertsons.argus.mim.exception.ArgusMimRuntimeException;
import com.albertsons.argus.mim.service.MIMAutomationService;
import com.microsoft.playwright.Page;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.mim","com.albertsons.argus.domain","com.albertsons.argus.mail.service"})
public class ArgusAutomationMimDirmon implements CommandLineRunner {
	private static final Logger LOG = LogManager.getLogger(ArgusAutomationMimDirmon.class);
	
	@Autowired
	public Environment environ;
	
	@Autowired
	private MIMAutomationService mimAutomationService;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ArgusAutomationMimDirmon.class, args);
		LOG.info("Shutdown Springboot . . .");
		System.exit(SpringApplication.exit(context, () -> 0));
	}
    
	@Override
	public void run(String... args) throws Exception {
		// Create instance variables
		String requestType = "Create Destination Template", uniqueNodeName = "FTDGV0105C3", 
				registryLabel = "SAFEWAY01", queueManager = "FTP4DV01";
		
		// Create template variables
		String requestName = "AWES-DIRMON-WTMiP-PRDCTLG-DAT-JBG-PMCJBG-WABASEM0000-PHX", label = "AWES-DIRMON-WTMiP-PRDCTLG-DAT-JBG-PMCJBG-WABASEM0000-PHX",
				sourceUid = "aw00es", targetUid = "damis01", transferType = "TEXT", writeMode = "REPLACE", ioAccess = "Traditional", 
				userArgs = "-sid "+ sourceUid + " -did " + targetUid + " -eid " + targetUid, sqm = "$XMDM_LOCAL_NODE_NAME$", dqm = "FTPHW039", 
				destFileName = "C:\\WTMiP\\Server\\JBG\\Send\\JBG-PMCJBG-WABASEM0000\\$XMDM_ORIGINAL_NAME$";
				
		// Create template - MVS Options
		String organization = "", recordFormat = "", lrecl = "", priQuantity = "", secQuantity = "",
				dirBlocks = "", spaceUnits = "";
		//String organization = "PDSE", recordFormat = "Variable Block", LRECL = "1", priQuantity = "3", secQuantity = "4",
		//		dirBlocks = "2", spaceUnits = "Cylinder";
		
		// Create Entry variables
		String monitoredDir = "\\\\pnv011fc3\\di$\\AWES\\UWARE\\DATA\\ExportProductCatalog\\Unsent", include = "EF*.*",
				requestTemplate = requestName;
		
		//Create Destination Template
		String destLabel = "ITSV-DIRMON-PSFT-JOBCODES", destName = destLabel + "_" + dqm, strdqm = "FTPHW039,FTPHW044";
		//String[] destinations = destLabel.split(","), dqms = strdqm.split(",");
		String[] destinations = mimAutomationService.getDestinationName(destLabel, strdqm);
		
		//Setup exit routine
		String exitRoutine = "Receiver-Post", targetServer = "UNIX", scriptName = "xmosys", exitMethod = "xmoSysCall", 
				userData = "C:\\WTMiP\\Server\\deflate" + "\\$XMDM_ORIGINAL_NAME$";
		
		//Verify Running MIM
		String sourceServer = "FTPHW044", destServer = "FTPHW039";
		
		//Page page = null;
		//Map<Integer, List<String>> testMap = mimAutomationService.readExcelValues("C:\\Users\\damis01\\Desktop\\Citrix Software Validation Result.xlsx");
		//mimAutomationService.inputMultipleDestinationDetails(page, uniqueNodeName, testMap);
		//mimAutomationService.inputMultipleDestinationDetails(page, spaceUnits, mimAutomationService.readExcelValues(environ.getProperty("argus.mim.destination.template.path")));
		
		try {
			LOG.info("Start MIM Dirmon . . .");
			
			Page page = mimAutomationService.navigateMIMLogin();
        	
			mimAutomationService.startMimDirmon(page);
			
	        switch (requestType) {
			case "Create Instance":
				
				mimAutomationService.createDirmonInstance(page, uniqueNodeName, registryLabel, queueManager);
	        	
				break;
			case "Create Template":
				
				mimAutomationService.createDirmonTemplate(page, requestName, destLabel, transferType, writeMode, ioAccess, 
						userArgs, exitRoutine, sqm, dqm, destFileName, organization, recordFormat, lrecl, priQuantity, secQuantity, 
						dirBlocks, spaceUnits, false);
				break;
			case "Create Entry":
			
				mimAutomationService.createDirmonEntry(page, uniqueNodeName, requestTemplate, monitoredDir, include, queueManager);
				
				break;
			case "Create Destination Template":
				
				mimAutomationService.createDestinationTemplate(page, destName, dqm, destFileName, userArgs, destLabel, 
						destinations, requestName, destLabel, transferType, writeMode, ioAccess, exitRoutine, sqm, 
						organization, recordFormat, lrecl, priQuantity, secQuantity, dirBlocks, spaceUnits, destServer);
				
				break;
			case "Setup Exit Routine":
				mimAutomationService.setupExitRoutine(page, exitRoutine, scriptName, exitMethod, queueManager, userData, destLabel, targetUid, uniqueNodeName);
				
				break;
			case "Disable Dirmon Label":
				mimAutomationService.disableDirmonLabel(page, uniqueNodeName, label);
				
				break;
			case "Delete Entry":
				mimAutomationService.deleteDirmonEntry(page, uniqueNodeName, label);
				
				break;
			case "Verify Running MIM":
				mimAutomationService.verifyRunningMIM(page, sourceServer, destServer);
				
				break;
			case "Verify Communication MIM":
				mimAutomationService.verifyCommunicationMIM(page, sourceServer, destServer);
				
				break;
			case "Verify Existing Entry":
				mimAutomationService.verifyExistingDirmonEntry(page, uniqueNodeName);
				
				break;
			default:
				break;
			}
	        
    		LOG.info("MIM Dirmon Creation Completed . . .");
    		
		} catch (Exception e) {
			throw new ArgusMimRuntimeException(e.getMessage());
		}	
	}
}
