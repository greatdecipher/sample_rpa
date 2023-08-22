package com.albertsons.argus.patchingctask.service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.client.RestClientException;

import com.albertsons.argus.patchingctask.ws.bo.ResponseGetChangeByIDBO;
import com.albertsons.argus.patchingctask.ws.bo.ResponseGetChangeListBO;
import com.albertsons.argus.patchingctask.ws.bo.ResponseSaveChangeBO;

public interface PatchingCtaskAutomationService {
    public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String MIM_LOGIN_URL = "playwright.uri.mim.login";
    public static final String LOG_FOLDER = "argus.mim.log.folder";
	public static final String ENV_COL_ID = "patching.excel.filter.env.col.id";
    public static final String CHG_GRP_COL_ID = "patching.excel.filter.chg.grp.col.id";

    public ResponseGetChangeListBO getChangeLists(String requestBody) throws RestClientException;
    public String createCtask(String requestBody) throws RestClientException;
    public ResponseGetChangeByIDBO getChangeById(String requestBody, String changeNumber) throws RestClientException;
    public ResponseSaveChangeBO saveChangeNumber(String requestBody, String changeNumber, 
        String automationId, String processingStatusId, String machineName, String userName) throws RestClientException;
    public Boolean downloadAttachment(String attachmentURL);
    public String getSysIdFromAttachmentUrl(String attachmentUrl);
    public List<String> filterExcelShet(XSSFSheet sheet, Long envColId, Long chgGrpcolId, String envFilterCrit, String chgGrpfilterCriteria);
    public void prepExcel(String fileName, String chgEnvironment, String chgNumber);
    public List<String> filterExcelShetByAppSupp(XSSFSheet sheet, Long envColId, Long chgGrpColId, Long appSuppColId, String envFilterCrit, String chgGrpfilterCrit, String appSuppFilterCrit);
    public String getFilteredExcelValues(XSSFSheet sheet, Long envColId, Long chgGrpColId, Long appSuppColId, String envFilterCrit, String chgGrpfilterCrit, String appSuppFilterCrit);
    public void delay(long seconds);
}