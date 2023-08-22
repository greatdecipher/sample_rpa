package com.albertsons.argus.patchingctask.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.patchingctask.service.PatchingCtaskAutomationService;
import com.albertsons.argus.patchingctask.ws.bo.ResponseGetChangeByIDBO;
import com.albertsons.argus.patchingctask.ws.bo.ResponseGetChangeListBO;
import com.albertsons.argus.patchingctask.ws.bo.ResponseSaveChangeBO;

@Service
public class PatchingCtaskAutomationServiceImpl implements PatchingCtaskAutomationService{
    public static final Logger LOG = LogManager.getLogger(PatchingCtaskAutomationServiceImpl.class);
	@Autowired
    private Environment environ;
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
    private AutomationService<ResponseGetChangeListBO> jsonResponseGetChangeListBOService;

    @Autowired
    private AutomationService<ResponseGetChangeByIDBO> jsonResponseGetChangeByIDBOService;
    
    @Autowired
    private AutomationService<ResponseSaveChangeBO> jsonResponseSaveChangeNumberService;

	@Override
    public String createCtask(String requestBody) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method createCtask(). . .");
		
        return restTemplate.exchange(environ.getProperty("patching.web.service.url.create.ctask"), HttpMethod.PUT, getHttpEntity(requestBody,"Token"),String.class).getBody() ;
    }

    @Override
    public ResponseGetChangeListBO getChangeLists(String requestBody) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method getChangeList(). . .");

        return jsonResponseGetChangeListBOService.toJson(restTemplate.exchange(environ.getProperty("patching.web.service.url.get.list"), HttpMethod.POST, getHttpEntity(requestBody,"Token"),String.class).getBody());
    }
    
    @Override
    public ResponseGetChangeByIDBO getChangeById(String requestBody, String changeNumber) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method getChangeList(). . .");

        return jsonResponseGetChangeByIDBOService.toJson(restTemplate.exchange(environ.getProperty("patching.web.service.change.number.get"), 
            HttpMethod.GET, getHttpEntityDBOperations(requestBody,"Token", changeNumber, "", "", 
                "", ""),String.class).getBody());
    }

    @Override
    public ResponseSaveChangeBO saveChangeNumber(String requestBody, String changeNumber, 
        String automationId, String processingStatusId, String machineName, String userName) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method getChangeList(). . .");

        return jsonResponseSaveChangeNumberService.toJson(restTemplate.exchange(environ.getProperty("patching.web.service.change.number.save"), 
            HttpMethod.POST, getHttpEntityDBOperations(requestBody,"Token", changeNumber, automationId,
                processingStatusId, machineName, userName),String.class).getBody());
    }

    private HttpEntity<Object> getHttpEntity(String requestBody,String headerName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(headerName, environ.getProperty("patching.web.service.auth.header.value"));
		return new HttpEntity<>(requestBody,headers);
    }
    
    private HttpEntity<Object> getHttpEntityDBOperations(String requestBody,String headerName, String changeNumber, 
        String automationId, String processingStatusId, String machineName, String userName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(headerName, environ.getProperty("patching.web.service.auth.header.value.esca.common"));
        if (!automationId.isEmpty()) {
            headers.set("ChangeNumber", changeNumber);
            headers.set("AutomationId", automationId);
            headers.set("ProcessingStatusId", processingStatusId);
            headers.set("MachineName", machineName);
            headers.set("UserName", userName);
        }
        else {
            headers.set("ChangeNumber", changeNumber);
        }
		return new HttpEntity<>(requestBody,headers);
    }

    @Override
	public Boolean downloadAttachment(String attachmentURL){
		Boolean bool = true;
			try {
			
				URL myUrl = new URL(attachmentURL.replaceAll("[\\n\\t ]", ""));
				System.out.println(myUrl.toString());
				HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
				conn.setDoOutput(true);
				conn.setReadTimeout(60000);
				conn.setConnectTimeout(60000);
				//conn.setUseCaches(false);
				//conn.setAllowUserInteraction(false);
				//conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept-Charset", "UTF-8");
				conn.setRequestMethod("GET");
				String basicAuth = environ.getProperty("patching.web.service.auth.header.value.basic");
				conn.setRequestProperty ("Authorization", basicAuth);
				
				InputStream in = conn.getInputStream();
				FileOutputStream out = new FileOutputStream(environ.getProperty("patching.web.service.download.attachment.path"));
				int c;
				byte[] b = new byte[1024];

				while ((c = in.read(b)) != -1){
					out.write(b, 0, c);
				}
				
				in.close();
				out.close();
                LOG.info("Snow attachment successfully downloaded. . .");
			} catch (Exception e) {
				LOG.info("Error downloading attachment. . .");
				LOG.error(e);
				bool = false;
			}

			return bool;
	}

    @Override
	public String getSysIdFromAttachmentUrl(String attachmentUrl) {
		String outputUrl = "", sysId ="";
		sysId = attachmentUrl.substring(attachmentUrl.indexOf("sys_id=") + 7);
        LOG.info("Attachment SysId: " + sysId);
		outputUrl = environ.getProperty("patching.web.service.attachment.url.get").replace("{sys_id}", sysId).replace(",","");
		return outputUrl;
	}
    
    @Override 
    public void prepExcel(String fileName, String chgEnvironment, String chgNumber) {
        FileInputStream file = null;
        XSSFSheet sheet = null;
        String prePatchShortDesc = "Pre-patching Application Task - ", postPatchShortDesc = "Post-patching Application Task - ", ciName, chgAssignGroup = environ.getProperty("patching.change.assign.group");
        Integer ctr = 0;

        try {
            file = new FileInputStream(new File(fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheet(environ.getProperty("patching.excel.sheet.name"));

            List<String> lstUniqueAppSupportGroup = filterExcelShet(sheet, Long.parseLong(environ.getProperty(ENV_COL_ID)), 
                Long.parseLong(environ.getProperty(CHG_GRP_COL_ID)), chgEnvironment, chgAssignGroup);
            List<String> lstAppCodesPerSuppGroup = new ArrayList<>();

            if (lstUniqueAppSupportGroup.size() > 0) {
                for (String appSuppGrp : lstUniqueAppSupportGroup) {
                    ctr = 0;
                    lstAppCodesPerSuppGroup = filterExcelShetByAppSupp(sheet, 2L, 44L, 64L, chgEnvironment, chgAssignGroup, appSuppGrp);
                    
                    prePatchShortDesc = "Pre-patching Application Task - ";
                    postPatchShortDesc = "Post-patching Application Task - ";
                    
                    for (String appCode : lstAppCodesPerSuppGroup) {
                        if (ctr == lstAppCodesPerSuppGroup.size() -1) {
                            prePatchShortDesc = prePatchShortDesc + appCode;
                            postPatchShortDesc = postPatchShortDesc + appCode;
                        }
                        else {
                            prePatchShortDesc = prePatchShortDesc + appCode + ",";
                            postPatchShortDesc = postPatchShortDesc + appCode + ",";
                        }
                        ctr ++;
                    }
    
                    ciName = getFilteredExcelValues(sheet, 2L, 44L, 64L, chgEnvironment, chgAssignGroup, appSuppGrp);
                    
                    try {
                        LOG.info("Creating Pre-Patching Ctask. . .");
                        LOG.info("Request Body: {" +
                            "\"change\": \"" + chgNumber + "\"," +
                            "\"short_description\": \"" + prePatchShortDesc + "\"," +
                            "\"assignment_group\": \"" + appSuppGrp + "\"," +
                            "\"configuration_item\": \"" + ciName + "\"" +
                            "}");
        
                        createCtask("{" +
                            "\"change\": \"" + chgNumber + "\"," +
                            "\"short_description\": \"" + prePatchShortDesc + "\"," +
                            "\"assignment_group\": \"" + appSuppGrp + "\"," +
                            "\"configuration_item\": \"" + ciName + "\"" +
                            "}");
    
                        LOG.info("Creating Post-Patching Ctask. . .");
                        LOG.info("Request Body: {" +
                            "\"change\": \"" + chgNumber + "\"," +
                            "\"short_description\": \"" + postPatchShortDesc + "\"," +
                            "\"assignment_group\": \"" + appSuppGrp + "\"," +
                            "\"configuration_item\": \"" + ciName + "\"" +
                            "}");
        
                        createCtask("{" +
                            "\"change\": \"" + chgNumber + "\"," +
                            "\"short_description\": \"" + postPatchShortDesc + "\"," +
                            "\"assignment_group\": \"" + appSuppGrp + "\"," +
                            "\"configuration_item\": \"" + ciName + "\"" +
                            "}");
                    } catch (Exception e) {
                        LOG.error("Error in creating CTasks for CHG - " + chgNumber + ", App Support Group - " + appSuppGrp + ", CI Name - " + ciName);
                    }
                    
                }
                LOG.info("Patching CTask Creation for CHG - " + chgNumber + " completed . . .");
            }
            else {
                LOG.info("List of App Support Groups not found. . .");
            }
            
            workbook.close();
            file.close();
            
        } catch (Exception e) {
            LOG.error("Error in setting file variable, filterExcelShet method. . .");
            LOG.error(e);
            return;
        } 
        
    }

    @Override
    public List<String> filterExcelShet(XSSFSheet sheet, Long envColId, Long chgGrpcolId, String envFilterCrit, String chgGrpfilterCriteria) {
        
        Boolean isMatchCrit1, isMatchCrit2;
        List<String> appSupportList = new ArrayList<>();
        List<String> appSupportUniqueList = new ArrayList<>();
        Integer rowCtr = 0;
        try {
            /* Add Filter Conditions to List */
            List<String> list1 = new ArrayList<String>();
            list1.add(envFilterCrit);
            List<String> list2 = new ArrayList<String>();
            list2.add(chgGrpfilterCriteria);

            /* Step-6: Loop through Rows and Apply Filter */
            for(Row r : sheet) {
                if (r.getRowNum() == 0) {
                    continue;
                }
               
                isMatchCrit1 = false;
                isMatchCrit2 = false;
                for (Cell c : r) {

                    if (c.getColumnIndex()==envColId && list1.contains(c.getStringCellValue().trim())) {
                        isMatchCrit1 = true;
                    }

                    if (c.getColumnIndex()==chgGrpcolId && list2.contains(c.getStringCellValue().trim())) {
                        isMatchCrit2 = true;
                    }

                    if (isMatchCrit1 && isMatchCrit2) {
                        if (c.getColumnIndex()==64) {
                            rowCtr += 1;
                            appSupportList.add(c.getStringCellValue().trim());
                        }
                    }
       
                }
            }

            appSupportUniqueList = appSupportList.stream().distinct().collect(Collectors.toList());
            

        } catch (Exception e) {
            LOG.error("Error in filterExcelShet method. . .");
            LOG.error(e);
        }
        
        return appSupportUniqueList;
        
    }

    @Override
    public List<String> filterExcelShetByAppSupp(XSSFSheet sheet, Long envColId, Long chgGrpColId, Long appSuppColId, String envFilterCrit, String chgGrpfilterCrit, String appSuppFilterCrit) {
        
        Boolean isMatchCrit1, isMatchCrit2, isMatchCrit3;
        List<String> appCodesList = new ArrayList<>();
        List<String> appCodesUniqueList = new ArrayList<>();
        
        try {
            //------Add filter criteria to lists for comparison------//
            //------Filter1=environment, Filter2=change assignment group, Filter3= app support------// 
            List<String> list1 = new ArrayList<String>();
            list1.add(envFilterCrit);
            List<String> list2 = new ArrayList<String>();
            list2.add(chgGrpfilterCrit);
            List<String> list3 = new ArrayList<String>();
            list3.add(appSuppFilterCrit);

            for(Row r : sheet) {
                if (r.getRowNum() == 0) {
                    continue;
                }
                
                isMatchCrit1 = false;
                isMatchCrit2 = false;
                isMatchCrit3 = false;

                //------loop through cells in row and check if filters match------//
                for (Cell c : r) {
                    if (c.getColumnIndex()==envColId && list1.contains(c.getStringCellValue().trim())) {
                        isMatchCrit1 = true;
                    }

                    if (c.getColumnIndex()==chgGrpColId && list2.contains(c.getStringCellValue().trim())) {
                        isMatchCrit2 = true;
                    }

                    if (c.getColumnIndex()==appSuppColId && list3.contains(c.getStringCellValue().trim())) {
                        isMatchCrit3 = true;
                    }
                }

                //------Add appcode to list if all 3 criteria are satisfied------//
                if (isMatchCrit1 && isMatchCrit2 && isMatchCrit3) {
                    appCodesList.add(r.getCell(12).getStringCellValue());
                }
            }
            
            //------Return only unique app codes from list------//
            appCodesUniqueList = appCodesList.stream().distinct().collect(Collectors.toList());
            

        } catch (Exception e) {
            LOG.error("Error in filterExcelShetByAppSupp method. . .");
            LOG.error(e);
        }
        
        return appCodesUniqueList;
        
    }

    @Override
    public String getFilteredExcelValues(XSSFSheet sheet, Long envColId, Long chgGrpColId, Long appSuppColId, String envFilterCrit, String chgGrpfilterCrit, String appSuppFilterCrit) {
        
        Boolean isMatchCrit1, isMatchCrit2, isMatchCrit3;
        String ciName = "";

        try {
            //------Add filter criteria to lists for comparison------//
            //------Filter1=environment, Filter2=change assignment group, Filter3= app support------// 
            List<String> list1 = new ArrayList<String>();
            list1.add(envFilterCrit);
            List<String> list2 = new ArrayList<String>();
            list2.add(chgGrpfilterCrit);
            List<String> list3 = new ArrayList<String>();
            list3.add(appSuppFilterCrit);

            for(Row r : sheet) {
                if (r.getRowNum() == 0) {
                    continue;
                }
                
                isMatchCrit1 = false;
                isMatchCrit2 = false;
                isMatchCrit3 = false;

                //------loop through cells in row and check if filters match------//
                for (Cell c : r) {
                    if (c.getColumnIndex()==envColId && list1.contains(c.getStringCellValue().trim())) {
                        isMatchCrit1 = true;
                    }

                    if (c.getColumnIndex()==chgGrpColId && list2.contains(c.getStringCellValue().trim())) {
                        isMatchCrit2 = true;
                    }

                    if (c.getColumnIndex()==appSuppColId && list3.contains(c.getStringCellValue().trim())) {
                        isMatchCrit3 = true;
                    }
                }

                //------Add appcode to list if all 3 criteria are satisfied------//
                if (isMatchCrit1 && isMatchCrit2 && isMatchCrit3) {
                    Cell cell = r.getCell(0);
                    ciName = cell.getStringCellValue();
                    break;
                }
            }
            
        } catch (Exception e) {
            LOG.error("Error in getFilteredExcelValues method. . .");
            LOG.error(e);
        }
        
        return ciName;
        
    }

    @Override
	public void delay(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
   
}