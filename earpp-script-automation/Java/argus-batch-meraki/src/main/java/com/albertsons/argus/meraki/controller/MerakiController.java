package com.albertsons.argus.meraki.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.albertsons.argus.domain.bo.generated.ARPEntries;
import com.albertsons.argus.domain.bo.generated.Internet1;
import com.albertsons.argus.domain.bo.generated.Internet2;
import com.albertsons.argus.domain.bo.generated.MerakiBO;
import com.albertsons.argus.domain.exception.DomainException;
import com.albertsons.argus.domain.playwright.service.impl.PlaywrightAutomationServiceImpl;
import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.meraki.api.MerakiAPI;
import com.albertsons.argus.meraki.exception.MerakiBadRequestArpException;
import com.albertsons.argus.meraki.exception.MerakiException;
import com.albertsons.argus.meraki.exception.MerakiWebserviceException;
import com.albertsons.argus.meraki.service.MerakiService;
import com.albertsons.argus.meraki.service.MerakiWebService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author kbuen03
 * @since 04/06/22
 * @version 1.0
 * 
 */
@RestController
public class MerakiController implements MerakiAPI {
	private static final Logger LOG = LogManager.getLogger(MerakiController.class);

	@Autowired
	private MerakiService merakiService;

	@Autowired
	private AutomationService<MerakiBO> automationService;

	@Autowired
	private MerakiWebService merakiWebService;

	@Autowired
	private PlaywrightAutomationServiceImpl playwrightAutomationServiceImpl;

	@Autowired
	private Environment environment;

	@Override
	public ResponseEntity<Object> getARPRequest(String token, String merakiArpRequest) {
		MerakiBO merakiBO = new MerakiBO();
		ARPEntries arpEntries = new ARPEntries();
		AutomationUtil util = new AutomationUtil();

		List<String> merakiRequestLists = new ArrayList<>();
		// List<String> emailSubjectLists = new ArrayList<>();

		// String delimiterTilde = environment.getProperty("argus.meraki.batch.mail.received.all.subject.delimiter");
		String delimiterPipe = environment.getProperty("argus.meraki.batch.mail.received.subject.delimiter");
		String inputSearchProp = environment.getProperty("argus.meraki.searchDashboard.name.input.value");
		String toolsTabPropValue = environment.getProperty("argus.meraki.clickToolsTab.name.text.value");

		String[] getHeaderArr = environment.getProperty("mail.argus.meraki.headers", String[].class);
		String[] toArr = environment.getProperty("mail.argus.meraki.recipients", String[].class);

		String[] toSupport = environment.getProperty("mail.argus.meraki.support.recipients", String[].class);

		String merakiReq = "";
		String orgStr = "";
		String storeStr = "";
		String guidStr = "";

		List<String> headerList = Arrays.asList(getHeaderArr);

		if (StringUtils.isNotBlank(merakiArpRequest)) {
			merakiRequestLists = util.getStringTokenLists(merakiArpRequest, delimiterPipe);

			if (!merakiRequestLists.isEmpty()) {
				orgStr = StringUtils.trim(merakiRequestLists.get(0));
				storeStr = StringUtils.trim(merakiRequestLists.get(1));
				merakiReq = StringUtils.trim(merakiRequestLists.get(2));
				guidStr = StringUtils.trim(merakiRequestLists.get(3));
			}

			String requestIdStr = orgStr + "|" + storeStr + "|" + merakiReq + "|" + guidStr;

			LOG.log(Level.INFO, () -> "Request Id: " + requestIdStr);

			merakiBO.setRequestId(requestIdStr);

			String navigateOrgLink = orgStr;

			String inputValue = storeStr;

			List<Internet1> internet1List = new ArrayList<>();
			List<Internet2> internet2List = new ArrayList<>();

			Browser browser = null;

			try {
				// Open Browser
				browser = playwrightAutomationServiceImpl.openBrowser();
			} catch (DomainException me) {
				LOG.log(Level.ERROR, () -> "Playwright open browser error: " + me);
				merakiService.sendMerakiEmail(arpEntries,
						requestIdStr
								+ " - MerakiException",
						toSupport, me.getMessage());

				throw new MerakiWebserviceException("Meraki Internal Server Error. Please checked the logs");
			}
			try {
				// Login
				Page page = merakiService.navigateLoginSSL(browser);
				// Click organization Page
				merakiService.organizationPage(page, navigateOrgLink);
				// Search Page
				merakiService.searchDashboard(page, inputSearchProp, inputValue);
				// Select and click the search results
				merakiService.clickResultDashboard(page, inputValue);
				// Click Tools tab
				merakiService.clickToolsTab(page, toolsTabPropValue);
				// Click APR Btns
				merakiService.clickAPRBtn(page);

				internet1List = merakiService.internetOneOutput(page);
				try {
					internet2List = merakiService.internetTwoOutput(page);
				} catch (MerakiException me) {
					LOG.log(Level.ERROR, () -> "Playwright error - internetTwoOutput: " + me);
				}
				// closed Browser
				playwrightAutomationServiceImpl.closedBrowser(browser);
			} catch (MerakiException me) {
				LOG.log(Level.ERROR, () -> "Playwright error: " + me);
				merakiService.sendMerakiEmail(arpEntries,
						requestIdStr
								+ " - MerakiException",
						toSupport, me.getMessage());

						playwrightAutomationServiceImpl.closedBrowser(browser);

				throw new MerakiWebserviceException("Meraki Internal Server Error. Please checked the logs");
			}

			arpEntries.setInternet1(internet1List);
			arpEntries.setInternet2(internet2List);

			merakiBO.setARPEntries(arpEntries);

			String merakiJson = automationService.toJsonString(merakiBO);

			try {
				String webServiceOutput = merakiWebService.sendARPRequest(merakiJson);
				LOG.log(Level.INFO, () -> "webServiceOutput: " + webServiceOutput);

				if (webServiceOutput.contains("200")) {
					merakiService.sendMerakiEmail(arpEntries,
							requestIdStr, toArr, util.toMerakiOutputHtmlString(arpEntries, headerList));

				} else {
					merakiService.sendMerakiEmail(arpEntries,
							requestIdStr
									+ " - Webservice Failed",
							toArr, util.toMerakiOutputHtmlString(arpEntries, headerList));

					throw new MerakiBadRequestArpException("Meraki ARP Webservice Bad Request: "+webServiceOutput);
				}
			} catch (MerakiException me) {
				LOG.log(Level.ERROR, () -> "Webservice error: " + me);
				merakiService.sendMerakiEmail(arpEntries,
						requestIdStr
								+ " - Webservice Failed",
						toArr, util.toMerakiOutputHtmlString(arpEntries, headerList));
				
				
				throw new MerakiWebserviceException("Meraki ARP Webservice Failed: "+me);
			}
			LOG.log(Level.DEBUG, () -> "merakiJson: " + merakiJson);
		} else {
			// TODO: Handle Error
		}
		// getAllSubject = "Retail-eCert | LAB_9880 | Meraki ARP Request | 721138890334881987~IMT-SWE | SWE_2390 | Meraki ARP Request |
		// 721138890334881987";

		return ResponseEntity.ok("200 OK");
	}

}
