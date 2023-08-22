
package com.albertsons.argus.hire.service.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.exception.DomainException;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.LoginService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.hire.exception.HirePlaywrightException;
import com.albertsons.argus.hire.service.HirePlaywrightService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;

@Service
public class HirePlaywrightServiceImpl implements HirePlaywrightService {
        private static final Logger LOG = LogManager.getLogger(HirePlaywrightServiceImpl.class);
        @Autowired
        private LoginService domainOracleService;
        @Autowired
        private Environment environment;
        @Autowired
        private PlaywrightAutomationService playwrightAutomationService;

        public void browseStraightProcessing(String scenario, String requestNumber, String empId, String assignNumber,
                        String newHireDt) throws HirePlaywrightException {
                Browser browser = openBrowser();
                Page page = null;
                try {
                        page = navigateMyClientGroup(browser);
                        // WorkRelationShip
                        navigateClientGroupPage(page, CLIENTGRP_SHOWMORE);
                        navigateWorkRelationship(page, empId);
                        // navigateHomeLink(page);
                        // Edit WorkRelationship Page
                        navigateEditWorkRtnShip(page,newHireDt);
                        // PersonManagement
                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigatePersonManagementPage(page, empId, assignNumber, newHireDt, false, false);
                        navigateHomeLink(page);
                        // Change Assignment
                        navigateClientGroupPage(page, CLIENTGRP_SHOWMORE);
                        navigateChangeAssgmntPage(page, empId, assignNumber, newHireDt);
                        navigateEditChangeAssignment(page, newHireDt);
                        // navigateHomeLink(page);
                        
                        // Person Management
                         //To refresh the page
                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigateHomeLink(page);
                        //end

                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigatePersonManagementPage(page, empId, assignNumber, newHireDt, true, false);
                        navigateHomeLink(page);

                } catch (InterruptedException e) {
                        LOG.log(Level.ERROR, () -> e);
                        new HirePlaywrightException(e.getMessage());
                } finally {
                        LOG.log(Level.DEBUG, () -> "closed browser. . .");
                        if (page != null)
                                page.close();
                        // Closed Broswer
                        playwrightAutomationService.closedBrowser(browser);
                }
        }

        @Override
        public void browseSyncPositionRowExists(String scenario, String requestNumber, String empId,
                        String assignNumber,
                        String newHireDt) throws HirePlaywrightException {
                Browser browser = openBrowser();
                Page page = null;
                try {
                        page = navigateMyClientGroup(browser);

                        // Cancel Work Relationship
                        navigateClientGroupPage(page, CLIENTGRP_SHOWMORE);
                        navigateCancelWorkRelationship(page, empId);
                        navigateHomeLink(page);
                        // Person Management
                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigatePersonManagementPage(page, empId, assignNumber, newHireDt, false, true);
                        // ChangeAssignment
                        navigateClientGroupPage(page, CLIENTGRP_SHOWMORE);
                        navigateChangeAssgmntPage(page, empId, assignNumber, newHireDt);
                        navigateEditChangeAssignment(page, newHireDt);
                        // navigateHomeLink(page);
                        
                        // Person Management
                         //To refresh the page
                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigateHomeLink(page);
                        //end
                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigatePersonManagementPage(page, empId, assignNumber, newHireDt, true, false);
                } catch (InterruptedException e) {
                        LOG.log(Level.ERROR, () -> e);
                        new HirePlaywrightException(e.getMessage());
                } finally {
                        LOG.log(Level.DEBUG, () -> "closed browser. . .");
                        if (page != null)
                                page.close();
                        // Closed Broswer
                        playwrightAutomationService.closedBrowser(browser);
                }
        }

        @Override
        public void browseEmpNewHireDateBeforePendingWork(String scenario, String requestNumber, String empId,
                        String assignNumber, String newHireDt) throws HirePlaywrightException {
                Browser browser = openBrowser();
                Page page = null;
                try {
                        page = navigateMyClientGroup(browser);

                        // Termination Page
                        navigateClientGroupPage(page, CLIENTGRP_SHOWMORE);
                        navigateTerminationPage(page, empId);
                        navigateEditTerminationPage(page, newHireDt);
                        // page.reload();

                        // WorkRelationship Page
                        //To refresh the page
                        navigateClientGroupPage(page, CLIENTGRP_SHOWMORE);
                        navigateHomeLink(page);
                        //End
                        navigateClientGroupPage(page, CLIENTGRP_SHOWMORE);
                        navigateWorkRelationship(page, empId);

                        // Edit WorkRelationship Page
                        navigateEditWorkRtnShip(page,newHireDt);

                        // PersonManagement Page
                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigatePersonManagementPage(page, empId, assignNumber, newHireDt, false, false);
                        navigateHomeLink(page);

                        // Change Assignment
                        navigateClientGroupPage(page, CLIENTGRP_SHOWMORE);
                        navigateChangeAssgmntPage(page, empId, assignNumber, newHireDt);
                        navigateEditChangeAssignment(page, newHireDt);
                        // navigateHomeLink(page);

                        // Person Management
                         //To refresh the page
                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigateHomeLink(page);
                        //end
                        navigateClientGroupPage(page, CLIENTGRP_PERSON_MANAGEMENT);
                        navigatePersonManagementPage(page, empId, assignNumber, newHireDt, true, false);

                } catch (InterruptedException e) {
                        LOG.log(Level.ERROR, () -> "browseEmpNewHireDateBeforePendingWork() exception: " + e);
                        new HirePlaywrightException(e.getMessage());
                } finally {
                        LOG.log(Level.DEBUG, () -> "closed browser. . .");
                        // Closed Broswer
                        if (page != null)
                                page.close();

                        playwrightAutomationService.closedBrowser(browser);
                }
        }

        private Page navigateChangeAssgmntPage(Page page, String empId, String assignmentNo, String hireDate)
                        throws HirePlaywrightException {
                LOG.log(Level.DEBUG, () -> "navigateChangeAssgmntPage - start. . . .");
                AutomationUtil automationUtil = new AutomationUtil();
                String dateFormat = environment.getProperty("com.argus.domain.util.date.format");
                String timezone = environment.getProperty("com.argus.domain.util.date.timezone");

                // playwrightAutomationService.pageClick(page, "a", "target",
                // "my_org_chg_asgn");

                try {
                        List<ElementHandle> querySelAdvSearch = page
                                        .querySelectorAll("a:has-text('Change Assignment')");
                        if (!querySelAdvSearch.isEmpty()) {
                                for (ElementHandle elem : querySelAdvSearch) {
                                        if (!elem.isHidden()) {
                                                elem.click();
                                                break;
                                        }
                                }
                        }

                        // Click InputBox
                        playwrightAutomationService
                                        .pageLocatorWait(page, "input", "id",
                                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Srh::content'")
                                        .click();
                        playwrightAutomationService
                                        .pageLocatorWait(page, "input", "id",
                                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Srh::content'")
                                        .fill(empId);
                        // Click Advance Search
                        playwrightAutomationService.pageClick(page, "a", "id",
                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Cl'");

                        // Advanced Search Input box
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf05sf:hf03Inp::content'",
                                        empId);
                        // Advanced Magnifying Glass
                        playwrightAutomationService
                                        .pageLocatorWait(page, "a", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf05sf:hf03Cil'")
                                        .click();

                        // List<ElementHandle> querySelAdvSearch = page.querySelectorAll("td.x1i9");
                        // if (!querySelAdvSearch.isEmpty()) {
                        // playwrightAutomationService.pageClick(page, "", "", "td.x1i9");
                        // }

                        Calendar calToday = automationUtil.dateToCalendar(new Date(), timezone);
                        calToday.add(Calendar.YEAR, 1);
                        String effectiveAsOfDateOneYr = automationUtil.toDateString(calToday.getTime(), dateFormat,
                                        timezone);
                        LOG.log(Level.DEBUG,
                                        () -> "navigateChangeAssgmntPage - dateHireOneYr: " + effectiveAsOfDateOneYr);
                        // Fill Effective As-of Date
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Subj_id_8:hf01Dt::content'",
                                        effectiveAsOfDateOneYr);
                        playwrightAutomationService.pageLocatorWait(page, "button", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Subj_id_8:hf09Bn'")
                                        .click();
                        playwrightAutomationService.pageClick(page, "a", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf04Sbv:ap01Lv:0:ap01Pse:ap01Cl'");
                } catch (PlaywrightException pe) {
                        LOG.error("navigateChangeAssgmntPage() exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Change Assignment Page. Detail: "
                                                        + pe.getMessage());
                }

                LOG.log(Level.DEBUG, () -> "navigateChangeAssgmntPage - end. . . .");
                return page;
        }

        private Page navigateClientGroupPage(Page page, String section) throws HirePlaywrightException {
                try {
                        playwrightAutomationService.pageLocatorWait(page, "div", "aria-controls",
                                        "cluster_groupNode_workforce_management").click();

                        if (section.equalsIgnoreCase(CLIENTGRP_SHOWMORE)) {
                                playwrightAutomationService
                                                .pageLocatorWait(page, "a", "id",
                                                                "showmore_groupNode_workforce_management")
                                                .click();
                        } else if (section.equalsIgnoreCase(CLIENTGRP_PERSON_MANAGEMENT)) {
                                playwrightAutomationService.pageClick(page, "div", "id",
                                                "itemNode_workforce_management_person_management");
                        }
                } catch (PlaywrightException pe) {
                        LOG.error("navigateClientGroupPage() exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Client Group Page. Detail: "
                                                        + pe.getMessage());
                }

                return page;
        }

        private Page navigateWorkRelationship(Page page, String empId) throws HirePlaywrightException {
                LOG.log(Level.DEBUG, () -> "navigateWorkRelationship - start. . . .");
                AutomationUtil automationUtil = new AutomationUtil();
                String dateFormat = environment.getProperty("com.argus.domain.util.date.format");
                String timezone = environment.getProperty("com.argus.domain.util.date.timezone");
                try {
                        playwrightAutomationService.pageClick(page, "a", "target",
                                        "my_org_edit_work_relationship");
                        // Click InputBox
                        playwrightAutomationService
                                        .pageLocatorWait(page, "input", "id",
                                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Srh::content'")
                                        .click();
                        playwrightAutomationService
                                        .pageLocatorWait(page, "input", "id",
                                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Srh::content'")
                                        .fill(empId);

                        // Click Advance Search
                        playwrightAutomationService.pageClick(page, "a", "id",
                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Cl'");

                        // List<ElementHandle> querySelAdvSearch = page.querySelectorAll("td.x1i9");
                        // if (!querySelAdvSearch.isEmpty()) {
                        // playwrightAutomationService.pageClick(page, "", "", "td.x1i9");
                        // }
                        // Advanced Search Input box
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf05sf:hf03Inp::content'",
                                        empId);
                        // Advanced Magnifying Glass
                        playwrightAutomationService
                                        .pageLocatorWait(page, "a", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf05sf:hf03Cil'")
                                        .click();

                        Calendar calToday = automationUtil.dateToCalendar(new Date(), timezone);
                        calToday.add(Calendar.YEAR, 1);
                        String effectiveAsOfDateOneYr = automationUtil.toDateString(calToday.getTime(), dateFormat,
                                        timezone);
                        LOG.log(Level.DEBUG,
                                        () -> "navigateWorkRelationship - dateHireOneYr: " + effectiveAsOfDateOneYr);
                        // Fill Effective As-of Date
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Subj_id_8:hf01Dt::content'",
                                        effectiveAsOfDateOneYr);
                        playwrightAutomationService.pageLocatorWait(page, "button", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Subj_id_8:hf09Bn'")
                                        .click();
                        playwrightAutomationService.pageClick(page, "a", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf04Sbv:ap01Lv:0:ap01Pse:ap01Cl'");
                } catch (PlaywrightException pe) {
                        LOG.error("navigateWorkRelationship() exception" + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Work Relationship Page. Detail: "
                                                        + pe.getMessage());
                }

                LOG.log(Level.DEBUG, () -> "navigateWorkRelationship - end. . . .");
                return page;
        }

        private Page navigateEditWorkRtnShip(Page page, String newHireDt) throws HirePlaywrightException {
                try {
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Edit Work Relationship
                        // Continue
                        playwrightAutomationService.pageClick(page, "div", "id",
                                        "'pt1:atkfr1:0:rQuick:3:gp1Upl:UPsp1:SPsb2'");

                        // Inputbox
                        playwrightAutomationService.pageFill(page, "input", "name",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:lsdDt'",newHireDt);

                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);

                        page.mouse().move(100, 100);
                        playwrightAutomationService
                                        .pageLocatorWait(page, "div", "class", "'callToActionSubmit xeq p_AFTextOnly'")
                                        .click();
                } catch (PlaywrightException | InterruptedException pe) {
                        LOG.error("navigateEditWorkRtnShip() exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Edit Work Relationship Page. Detail: "
                                                        + pe.getMessage());
                }
                return page;
        }

        private Page navigateEditChangeAssignment(Page page, String newHireDt) throws HirePlaywrightException {
                try {
                        // Edit Work Relationship
                        // Continue
                        playwrightAutomationService.pageClick(page, "div", "id",
                                        "'pt1:atkfr1:0:rQuick:3:gp1Upl:UPsp1:SPsb2'");
                        // Inputbox
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:ipDt::content'",
                                        newHireDt);
                        page.mouse().move(100, 100);
                        // What's the way to change the assignment? - Combo Box
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis1:aidis::content'")
                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("What's the action name?")).locator("a").click();
                       TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.getByText("Date Correction").click();
                        TimeUnit.SECONDS.sleep(SET_LONGER_SEC_VALUE);
     
                        /* playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis1:aidis::content'")
                                        .focus();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis1:aidis::content'")
                                        .press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis1:aidis::content'")
                                        .press("ArrowDown"); 
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis1:aidis::content'")
                                        .press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis1:aidis::content'")
                                        .press("Enter");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE); */

                        //Why are you changing the assignment? - Combo Box
                         
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis2:aridis::content'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Why are you changing the assignment?")).locator("a").click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.getByText("Correction - Hire/Rehire Date").click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        
                        /*
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis2:aridis::content'")
                                        .focus();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis2:aridis::content'")
                                        .press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis2:aridis::content'")
                                        .press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis2:aridis::content'")
                                        .press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr0:0:sis2:aridis::content'")
                                        .press("Enter");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        */

                        // Continue
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPcb10'")
                                        .click();
                        // 2nd Conitunue Button
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPcb11'")
                                        .click();
                        // Fill Salary
                        /**
                         * playwrightAutomationService.pageLocatorWait(page, "input", "id",
                         * "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr2:1:salRgn:0:poSis:RsbSrh::content'")
                         * .focus();
                         * TimeUnit.SECONDS.sleep(SET_SEC_PENDING_WORKER);
                         * playwrightAutomationService.pageLocatorWait(page, "input", "id",
                         * "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr2:1:salRgn:0:poSis:RsbSrh::content'")
                         * .press("ArrowDown");
                         * TimeUnit.SECONDS.sleep(SET_SEC_PENDING_WORKER);
                         * playwrightAutomationService.pageLocatorWait(page, "input", "id",
                         * "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr2:1:salRgn:0:poSis:RsbSrh::content'")
                         * .press("ArrowDown");
                         * TimeUnit.SECONDS.sleep(SET_SEC_PENDING_WORKER);
                         * playwrightAutomationService.pageLocatorWait(page, "input", "id",
                         * "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr2:1:salRgn:0:poSis:RsbSrh::content'")
                         * .press("Enter");
                         */
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);

                        // playwrightAutomationService.pageFill(page, "input", "id",
                        // "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:r1Rgn:0:GPmtfr2:1:salRgn:0:poSis:RsbSrh::content'",
                        // "Hourly - US");

                        // Move mouse so enable submit button
                        page.mouse().move(100, 100);
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Submit
                        playwrightAutomationService
                                        .pageLocatorWait(page, "div", "id",
                                                        "'pt1:atkfr1:0:rQuick:4:up1Upl:UPsp1:SPsb2'")
                                        .click();
                } catch (PlaywrightException | InterruptedException pe) {
                        LOG.error("navigateEditChangeAssignment exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Edit Change Assignment Page. Detail: "
                                                        + pe.getMessage());
                }

                return page;
        }

        private Page navigatePersonManagementPage(Page page, String empId, String assignmentNumber, String hireDate,
                        boolean isHistory,
                        boolean isTerminatedEmp) throws HirePlaywrightException {
                LOG.log(Level.DEBUG, () -> "navigatePersonManagementPage - start. . . .");
                AutomationUtil automationUtil = new AutomationUtil();
                playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                "'pt1:_FOr1:1:_FONSr2:0:MAt1:0:pt1:Perso1:0:SP3:q1:value10::content'").fill(empId);
                String currentDateHireStr = playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                "'pt1:_FOr1:1:_FONSr2:0:MAt1:0:pt1:Perso1:0:SP3:q1:value50::content'").inputValue();
                String dateFormat = environment.getProperty("com.argus.domain.util.date.format");
                String timezone = environment.getProperty("com.argus.domain.util.date.timezone");
                String modifyYearDateHire = "";

                try {
                        Date currentHireDate = automationUtil.toDate(currentDateHireStr, dateFormat);
                        Calendar calCurrentHireDate = automationUtil.dateToCalendar(currentHireDate, timezone);
                        calCurrentHireDate.add(Calendar.YEAR, 1);
                        modifyYearDateHire = automationUtil.toDateString(calCurrentHireDate.getTime(), dateFormat,
                                        timezone);
                } catch (ParseException e) {
                        throw new HirePlaywrightException(e.getMessage());
                }

                try {
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:_FOr1:1:_FONSr2:0:MAt1:0:pt1:Perso1:0:SP3:q1:value50::content'",
                                        modifyYearDateHire);

                        if (isTerminatedEmp) {
                                playwrightAutomationService.pageLocatorWait(page, "label", "id",
                                                "'pt1:_FOr1:1:_FONSr2:0:MAt1:0:pt1:Perso1:0:SP3:q1:value40::Label0'")
                                                .click();

                                // Click Search
                                playwrightAutomationService
                                                .pageLocatorWait(page, "button", "id",
                                                                "'pt1:_FOr1:1:_FONSr2:0:MAt1:0:pt1:Perso1:0:SP3:q1::search'")
                                                .click();
                                try {
                                        TimeUnit.SECONDS.sleep(SET_LONGER_SEC_VALUE);
                                } catch (InterruptedException e) {
                                        throw new HirePlaywrightException(e.getMessage());
                                }

                                // terminated employee
                                List<ElementHandle> queryRows = page
                                                .querySelectorAll("table[summary='Search Results'] tr");

                                if (!queryRows.isEmpty()) {
                                        boolean isFoundPendingWorker = false;
                                        for (ElementHandle row : queryRows) {
                                                List<ElementHandle> cells = row.querySelectorAll("td");

                                                for (ElementHandle cell : cells) {
                                                        String text = cell.innerText();
                                                        // Search has Pending Worker
                                                        if (text.contains("Pending Worker")) {
                                                                ElementHandle nameLink = cell.querySelector("a");

                                                                nameLink.click();
                                                                isFoundPendingWorker = true;
                                                                break;
                                                        }
                                                }
                                                if (isFoundPendingWorker)
                                                        break;
                                        }
                                }
                        } else {
                                // Click Search
                                playwrightAutomationService
                                                .pageLocatorWait(page, "button", "id",
                                                                "'pt1:_FOr1:1:_FONSr2:0:MAt1:0:pt1:Perso1:0:SP3:q1::search'")
                                                .click();
                                try {
                                        TimeUnit.SECONDS.sleep(SET_LONGER_SEC_VALUE);
                                } catch (InterruptedException e) {
                                        throw new HirePlaywrightException(e.getMessage());
                                }
                                // Click Name
                                List<ElementHandle> queryRows = page
                                                .querySelectorAll("table[summary='Search Results'] tr");

                                if (!queryRows.isEmpty()) {
                                        boolean isFoundAssignment = false;
                                        for (ElementHandle row : queryRows) {
                                                List<ElementHandle> cells = row.querySelectorAll("td");

                                                for (ElementHandle cell : cells) {
                                                        String text = cell.innerText();
                                                        // Search has AssignmentNumber
                                                        if (text.contains(assignmentNumber)) {
                                                                ElementHandle nameLink = cell.querySelector("a");

                                                                nameLink.click();
                                                                isFoundAssignment = true;
                                                                break;
                                                        }
                                                }
                                                if (isFoundAssignment)
                                                        break;
                                        }
                                }

                                // playwrightAutomationService.pageLocatorWait(page, "a", "id",
                                // "'pt1:_FOr1:1:_FONSr2:0:MAt1:0:pt1:Perso1:0:SP3:table1:_ATp:table2:0:gl1'")
                                // .click();
                                // TODO: Validate New hire date UI vs from excel hire date

                        }
                        //Wait to finish the pages
                        try {
                                TimeUnit.SECONDS.sleep(20);
                        } catch (InterruptedException e) {
                                throw new HirePlaywrightException(e.getMessage());
                        }

                        if (isHistory) {
                                try {
                                        // TimeUnit.SECONDS.sleep(15);

                                        // playwrightAutomationService.pageLocatorWait(page, "div", "id",
                                        // "'_FOpt1:_FOr1:0:_FONSr2:0:MAt1:0:pt1:Manag1:0:AP1:historyButton'")
                                        // .click();

                                        // playwrightAutomationService.pageLocatorWait(page, "", "",
                                        // "a[role=\"button\"]:has-text(\"View History\")")
                                        // .click();
                                        page.getByRole(AriaRole.BUTTON,
                                                        new Page.GetByRoleOptions().setName("View History")).click();

                                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);

                                        playwrightAutomationService.pageLocatorWait(page, "div", "id",
                                                        "'_FOpt1:_FOr1:0:_FONSr2:0:MAt1:0:pt1:Manag1:0:AP1:r4:1:_dateP:_tbr::eoi'")
                                                        .click();

                                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                                        playwrightAutomationService.pageLocatorWait(page, "div", "id",
                                                        "'_FOpt1:_FOr1:0:_FONSr2:0:MAt1:0:pt1:Manag1:0:AP1:r4:1:_dateP:_dchTbr'")
                                                        .click();
                                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);

                                        // exit modal
                                        page.keyboard().press("Escape");
                                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                                        // Click Done
                                        playwrightAutomationService
                                                        .pageLocatorWait(page, "button", "id",
                                                                        "'_FOpt1:_FOr1:0:_FONSr2:0:MAt1:0:pt1:Manag1:0:AP1:d2::ok'")
                                                        .click();

                                } catch (InterruptedException e) {
                                        throw new HirePlaywrightException(e.getMessage());
                                }
                                // Table Management
                        } else if (isTerminatedEmp) {
                                try {
                                        TimeUnit.SECONDS.sleep(SET_LONGER_SEC_VALUE);
                                } catch (InterruptedException e) {
                                        throw new HirePlaywrightException(e.getMessage());
                                }
                                // Assignment Status
                                List<ElementHandle> queryHasTextInactive = page
                                                .querySelectorAll(
                                                                "div[id = '_FOpt1:_FOr1:0:_FONSr2:0:MAt1:0:pt1:Manag1:0:AP1:assig2:0:pfl1']:has-text('Inactive - Payroll Eligible')");
                                List<ElementHandle> queryHasTextActive = page
                                                .querySelectorAll(
                                                                "div[id = '_FOpt1:_FOr1:0:_FONSr2:0:MAt1:0:pt1:Manag1:0:AP1:assig2:0:pfl1']:has-text('Active - Payroll Eligible')");

                                if (!queryHasTextInactive.isEmpty()) {
                                        inactiveStatusPage(page);
                                        // Home Page
                                        navigateHomeLink(page);
                                        // Goto navigate pending worker
                                        navigatePendingWorkers(page, empId, hireDate);
                                } else if (!queryHasTextActive.isEmpty()) {
                                        // Home Page
                                        navigateHomeLink(page);
                                        navigatePendingWorkers(page, empId, hireDate);
                                } else {
                                        // No Assignment Status found
                                        LOG.error("navigatePersonManagementPage() exception: No Assignment Status found");
                                        throw new HirePlaywrightException(
                                                                "There is something wrong during navigate the Person Management Page. Detail: No Assignment Status found.");

                                }
                                // View Termination (has-text)
                        }
                } catch (PlaywrightException pe) {
                        LOG.error("navigatePersonManagementPage() exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Person Management Page. Detail: "
                                                        + pe.getMessage());
                }

                LOG.log(Level.DEBUG, () -> "navigatePersonManagementPage - end. . . .");
                return page;
        }

        // Click the section for myACI
        private void linkFindTextByClick(Page page, String name) throws PlaywrightException {
                List<ElementHandle> querySelAdvSearch = page.querySelectorAll("a:has-text(\"" + name + "\")");
                if (!querySelAdvSearch.isEmpty()) {
                        for (ElementHandle elem : querySelAdvSearch) {
                                if (!elem.isHidden()) {
                                        elem.click();
                                        break;
                                }
                        }
                }
        }

        // Active Payroll Status
        private Page navigatePendingWorkers(Page page, String empId, String hireDate) throws HirePlaywrightException {
                // playwrightAutomationService.pageClick(page, "a", "target",
                // "'my_org_mng_pending_worker'");
                try {
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        //linkFindTextByClick(page, "Pending Workers");
                        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Pending Workers")).click();
                        // Fill input box
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:1:r1:0:u1Upl:UPsp1:p1Pce:ffs1:hf05sf:hf03Inp::content'",
                                        empId);
                        // Press Enter to search
                        page.keyboard().press("Enter");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Click ... - option on result set
                        playwrightAutomationService.pageLocatorWait(page, "div", "id",
                                        "'pt1:atkfr1:0:rQuick:1:r1:0:u1Upl:UPsp1:p1Pce:ffs1:hf04Sbv:l1Lv:10:p1Pse:PSEt5::oc'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("Enter");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);

                        // Input box hire date
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPmtfr0:0:sdDt::content'",
                                        hireDate);

                        // Input box pending worker - What's the way to create the work relationship?What's the way to create the wor >> div >> nth=1
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPmtfr0:0:sis1:aidis::content'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.locator("text=*What's the way to create the work relationship?What's the way to create the wor >> div").nth(1).click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Click text=Hire >> nth=0
                        page.locator("text=Hire").first().click();
 
                       /*  playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPmtfr0:0:sis1:aidis::content'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("Enter");  */

                        TimeUnit.SECONDS.sleep(SET_LONGER_SEC_VALUE);
                        // Input Box hiring employee - Why are you adding a work Relationship?
                        page.locator("text=Why are you adding a work relationship?Press DOWN arrow key and then Press ENTER >> a").click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Click th:has-text("Hire")
                        page.locator("th:has-text(\"Hire\")").click();

                        /* playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPmtfr0:0:sis2:aridis::content'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("Enter"); */

                        page.mouse().move(100, 100);
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPcb10'")
                                        .click();

                        // Continue Button TODO:check queryselector
                        // String nationaIdType = playwrightAutomationService.pageLocatorWait(page,
                        // "input", "id",
                        // "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPmtfr1:1:nifRgn:0:typeSel::content'")
                        // .inputValue();

                        // if (StringUtils.isBlank(nationaIdType) ||
                        // nationaIdType.equalsIgnoreCase("Select a value")) {
                        // String body = "There is problem the process the Pending worker for: " + empId
                        // + " . The national id type is blank. ";
                        // try {
                        // sendReHire(EMAIL_SCENARIO_2_SUBJECT, body);
                        // } catch (ArgusMailException e) {
                        // throw new PlaywrightException(body);
                        // }
                        // }

                        // Continue Button 2
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPcb11'")
                                        .click();

                        // Continue Button 3
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPcb12'")
                                        .click();

                        // Continue Button 4
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPcb13'")
                                        .click();

                        // Continue Button 5
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPcb14'")
                                        .click();

                        // Continue Button 6
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPcb15'")
                                        .click();

                        // Continue Button 7
                        playwrightAutomationService
                                        .pageLocatorWait(page, "button", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:gpRgn:0:GPcb16'")
                                        .click();

                        // Submit button
                        playwrightAutomationService
                                        .pageLocatorWait(page, "div", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:cwhUpl:UPsp1:SPsb2'")
                                        .click();

                } catch (InterruptedException | PlaywrightException e) {
                        LOG.error("navigatePendingWorkers() exception: " + e);
                        // playwrightAutomationService.screenshotSaveFile(page,
                        // environment.getProperty("argus.hire.folder.ss"));
                        throw new PlaywrightException(
                                        "There is something wrong during navigate the Pending Workder Page. Detail: "
                                                        + e.getMessage());
                }
                return page;
        }

        private Page inactiveStatusPage(Page page) throws HirePlaywrightException {
                try {
                        // Task Panel
                        playwrightAutomationService
                                        .pageLocatorWait(page, "a", "id",
                                                        "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsdiHcmIntWaTasksId::disAcr'")
                                        .click();
                        // Click Work Relationship
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Work Relationship").setExact(true)).click();
                        // playwrightAutomationService.pageClick(page, "a", "id",
                        //                 "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTRaT:0:RAtl19'");
                        // Click Action Down Button
                        playwrightAutomationService.pageClick(page, "a", "id",
                                        "'_FOpt1:_FOr1:0:_FONSr2:0:MAt2:0:pt1:r1:0:pt1:SP1:edit::popEl'");

                        // Click Termination View
                        playwrightAutomationService
                                        .pageLocatorWait(page, "tr", "id",
                                                        "'_FOpt1:_FOr1:0:_FONSr2:0:MAt2:0:pt1:r1:0:pt1:SP1:vtBtn'")
                                        .click();
                        // Click Reverse Termination
                        playwrightAutomationService.pageLocatorWait(page, "button", "id",
                                        "'_FOpt1:_FOr1:0:_FONSr2:0:MAt2:1:r1:0:pt1:ViewT1:0:AP1:tt1:commandButton2'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Click Yes Button
                        playwrightAutomationService.pageLocatorWait(page, "button", "id",
                                        "'_FOpt1:_FOr1:0:_FONSr2:0:MAt2:1:r1:0:pt1:ViewT1:0:AP1:tt1:commandButton5'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Exit Dialogue box
                        page.keyboard().press("Escape");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                } catch (PlaywrightException | InterruptedException pe) {
                        LOG.error("inactiveStatusPage() exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Inactive Status Page. Detail: "
                                                        + pe.getMessage());
                }

                return page;
        }

        private Page navigateCancelWorkRelationship(Page page, String empId) throws HirePlaywrightException {
                LOG.log(Level.DEBUG, () -> "navigateCancelWorkRelationship - start. . . .");
                AutomationUtil automationUtil = new AutomationUtil();
                String dateFormat = environment.getProperty("com.argus.domain.util.date.format");
                String timezone = environment.getProperty("com.argus.domain.util.date.timezone");
                try {
                        // playwrightAutomationService.pageClick(page, "div", "target",
                        // "'my_org_cancel_wr'");
                        List<ElementHandle> querySelAdvSearch = page
                                        .querySelectorAll("a:has-text('Cancel Work Relationship')");
                        if (!querySelAdvSearch.isEmpty()) {
                                for (ElementHandle elem : querySelAdvSearch) {
                                        if (!elem.isHidden()) {
                                                elem.click();
                                                break;
                                        }
                                }
                        }

                        // Click InputBox
                        playwrightAutomationService
                                        .pageLocatorWait(page, "input", "id",
                                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Srh::content'")
                                        .click();
                        playwrightAutomationService
                                        .pageLocatorWait(page, "input", "id",
                                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Srh::content'")
                                        .fill(empId);

                        // Click Advance Search
                        playwrightAutomationService.pageClick(page, "a", "id",
                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Cl'");

                        // List<ElementHandle> querySelAdvSearch = page.querySelectorAll("td.x1i9");
                        // if (!querySelAdvSearch.isEmpty()) {
                        // playwrightAutomationService.pageClick(page, "", "", "td.x1i9");
                        // }
                        // Advanced Search Input box
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf05sf:hf03Inp::content'",
                                        empId);
                        // Advanced Magnifying Glass
                        playwrightAutomationService
                                        .pageLocatorWait(page, "a", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf05sf:hf03Cil'")
                                        .click();

                        Calendar calToday = automationUtil.dateToCalendar(new Date(), timezone);
                        calToday.add(Calendar.YEAR, 1);
                        String effectiveAsOfDateOneYr = automationUtil.toDateString(calToday.getTime(), dateFormat,
                                        timezone);
                        LOG.log(Level.DEBUG,
                                        () -> "navigateWorkRelationship - dateHireOneYr: " + effectiveAsOfDateOneYr);
                        // Fill Effective As-of Date
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Subj_id_8:hf01Dt::content'",
                                        effectiveAsOfDateOneYr);
                        playwrightAutomationService.pageLocatorWait(page, "button", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Subj_id_8:hf09Bn'")
                                        .click();
                        playwrightAutomationService.pageClick(page, "a", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf04Sbv:ap01Lv:0:ap01Pse:ap01Cl'");

                        // Click Submit
                        playwrightAutomationService
                                        .pageLocatorWait(page, "div", "id",
                                                        "'pt1:atkfr1:0:rQuick:3:cwrUpl:UPsp1:SPsb2'")
                                        .click();
                } catch (PlaywrightException pe) {
                        LOG.error("navigateCancelWorkRelationship() exception:" + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Cancel Work Relationship Page. Detail: "
                                                        + pe.getMessage());
                }

                return page;
        }

        private Page navigateHomeLink(Page page) throws HirePlaywrightException {
                try {
                        playwrightAutomationService.pageLocatorWait(page, "path", "d", "'M25 25V10L14 3L3 10V25H25Z'").click();
                } catch (PlaywrightException pe) {
                        LOG.error("navigateHomeLink() exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Home Link. Detail: "
                                                        + pe.getMessage());
                }
                return page;
        }

        public Browser openBrowser() throws HirePlaywrightException {
                Browser browser = null;
                try {
                        browser = playwrightAutomationService.openBrowser();
                } catch (DomainException e) {
                        LOG.log(Level.ERROR, () -> "openBrowser() exception: " + e);
                        throw new HirePlaywrightException("openBrowser: " + e.getMessage());
                }
                return browser;
        }

        public Page navigateMyClientGroup(Browser browser) throws InterruptedException {
                Page page = null;
                String oracleSaasUri = environment.getProperty("playwright.uri.oracle.myaci.saas.uri.hire");
                String clientGroupUri = oracleSaasUri
                                + environment.getProperty("playwright.uri.oracle.saas.client.groups.hire");
                String username = environment.getProperty("ldap.property.username.hire");
                String password = environment.getProperty("encrypted.ldap.property.password.hire");
                String mfaSecret = environment.getProperty("mfa.secret.key.hire");
                try {
                        BrowserContext browserContext = browser
                                        .newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
                        page = domainOracleService.loginPage(browser, browserContext, oracleSaasUri, username, password, mfaSecret);
                        TimeUnit.SECONDS.sleep(3);
                        page.navigate(clientGroupUri);
                } catch (InterruptedException | PlaywrightException e) {
                        LOG.error("navigateMyClientGroup() exception: " + e);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new InterruptedException(
                                        "There is something wrong during navigate the My Client Group Page. Detail: "
                                                        + e.getMessage());
                }
                // finally {
                // LOG.log(Level.DEBUG, () -> "closed browser. . .");
                // // Closed Broswer
                // playwrightAutomationService.closedBrowser(browser);
                // }
                return page;
        }

        private Page navigateTerminationPage(Page page, String empId) throws HirePlaywrightException {
                AutomationUtil automationUtil = new AutomationUtil();
                String dateFormat = environment.getProperty("com.argus.domain.util.date.format");
                String timezone = environment.getProperty("com.argus.domain.util.date.timezone");
                try {
                        List<ElementHandle> querySelAdvSearch = page.querySelectorAll("a:has-text('Termination')");
                        if (!querySelAdvSearch.isEmpty()) {
                                for (ElementHandle elem : querySelAdvSearch) {
                                        if (!elem.isHidden()) {
                                                elem.click();
                                                break;
                                        }
                                }
                        }

                        // Click InputBox
                        playwrightAutomationService
                                        .pageLocatorWait(page, "input", "id",
                                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Srh::content'")
                                        .click();
                        playwrightAutomationService
                                        .pageLocatorWait(page, "input", "id",
                                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Srh::content'")
                                        .fill(empId);
                        // Click Advance Search
                        playwrightAutomationService.pageClick(page, "a", "id",
                                        "'pt1:atkfr1:0:rQuick:1:hp01Upl:UPsp1:hp01Sis:hp01Cl'");

                        // Advanced Search Input box
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf05sf:hf03Inp::content'",
                                        empId);
                        // Advanced Magnifying Glass
                        playwrightAutomationService
                                        .pageLocatorWait(page, "a", "id",
                                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf05sf:hf03Cil'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);

                        // Clear Checkbox
                        page.getByTitle("Clear filter Assignment Status").click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Clear")).click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // clearCheckBox(page);
                        // clearCheckBox(page);

                        // Check Active
                        // playwrightAutomationService
                        // .pageLocatorWait(page, "input", "id",
                        // "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Smcj_id_6:_0'")
                        // .click();

                        playwrightAutomationService
                                        .pageLocatorWait(page, "label", "for",
                                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Smcj_id_6:_0'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Check Inactive
                        playwrightAutomationService
                                        .pageLocatorWait(page, "label", "for",
                                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Smcj_id_6:_1'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Contigent Worker
                        // playwrightAutomationService
                        // .pageLocatorWait(page, "label", "for",
                        // "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Smcj_id_7:_0'")
                        // .click();
                        // TimeUnit.SECONDS.sleep(SET_SEC_PENDING_WORKER);
                        // Employee
                        // playwrightAutomationService
                        // .pageLocatorWait(page, "label", "for",
                        // "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Smcj_id_7:_1'")
                        // .click();
                        // TimeUnit.SECONDS.sleep(SET_SEC_PENDING_WORKER);
                        // Non-Worker
                        // playwrightAutomationService
                        // .pageLocatorWait(page, "label", "for",
                        // "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Smcj_id_7:_2'")
                        // .click();
                        // TimeUnit.SECONDS.sleep(SET_SEC_PENDING_WORKER);
                        // Pending-Worker
                        playwrightAutomationService
                                        .pageLocatorWait(page, "label", "for",
                                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Smcj_id_7:_3'")
                                        .click();

                        Calendar calToday = automationUtil.dateToCalendar(new Date(), timezone);
                        calToday.add(Calendar.YEAR, 1);
                        String effectiveAsOfDateOneYr = automationUtil.toDateString(calToday.getTime(), dateFormat,
                                        timezone);
                        LOG.log(Level.DEBUG,
                                        () -> "navigateChangeAssgmntPage - dateHireOneYr: " + effectiveAsOfDateOneYr);
                        // Fill Effective As-of Date
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Subj_id_8:hf01Dt::content'",
                                        effectiveAsOfDateOneYr);
                        playwrightAutomationService.pageLocatorWait(page, "button", "id",
                                        "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf01Subj_id_8:hf09Bn'")
                                        .click();

                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Validate Pending worker
                        // List<ElementHandle> workerTypeElemList = page.querySelectorAll(
                        // "div[id='pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf04Sbv:ap01Lv:0:ap01Pse:ap05Lbl']:has-text('Termination')");

                        List<ElementHandle> workerTypeElemList = page.querySelectorAll(
                                        "div[id='pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf04Sbv:hf29Pgl']");

                        if (workerTypeElemList.isEmpty()) {
                                String body = "There is problem the process the Termination for: " + empId
                                                + " . The Worker Type is not Pending Worker. ";
                                playwrightAutomationService.screenshotSaveFile(page,
                                                environment.getProperty("argus.hire.folder.ss"));
                                throw new HirePlaywrightException(body);
                        } else {
                                boolean hasPendingWorker = false;
                                for (ElementHandle eh : workerTypeElemList) {
                                        if (eh.innerText().contains("Pending Worker")) {
                                                // Pending Worker found set to true
                                                hasPendingWorker = true;
                                                String searchNameAttribute = "Name";

                                                String nameStr = StringUtils.substring(eh.innerText(),
                                                                eh.innerText().indexOf(searchNameAttribute)
                                                                                + searchNameAttribute.length(),
                                                                eh.innerText().indexOf("Business"));
                                                LOG.info("Pending worker found : "+nameStr);
                                                linkFindTextByClick(page, StringUtils.trimToEmpty(nameStr));

                                        }
                                }
                                if (!hasPendingWorker) {
                                        String body = "There is problem the process the Termination for: " + empId
                                                        + " . The Worker Type is not Pending Worker. ";
                                        playwrightAutomationService.screenshotSaveFile(page,
                                                        environment.getProperty("argus.hire.folder.ss"));
                                        throw new HirePlaywrightException(body);
                                }
                                LOG.info("Enable Delay . . ");
                                TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        }
                        // Click link
                        // playwrightAutomationService.pageClick(page, "a", "id",
                        // "'pt1:atkfr1:0:rQuick:2:ap01Upl:UPsp1:ap01Pce:ap01Fs:hf04Sbv:ap01Lv:0:ap01Pse:ap01Cl'");
                } catch (PlaywrightException | InterruptedException pe) {
                        LOG.error("navigateTerminationPage() exception: " + pe);

                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Termination Page. Detail: "
                                                        + pe.getMessage());
                }

                return page;
        }

        // Clear - Assignment Status and Worker Type
        private Page clearCheckBox(Page page) throws HirePlaywrightException {
                List<ElementHandle> queryClearAdvSearch = page.querySelectorAll("a:has-text('Clear')");
                try {
                        if (!queryClearAdvSearch.isEmpty()) {
                                for (ElementHandle elem : queryClearAdvSearch) {
                                        if (!elem.isHidden()) {
                                                elem.click();
                                                try {
                                                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                                                } catch (InterruptedException e) {
                                                        throw new HirePlaywrightException(e.getMessage());
                                                }
                                        }
                                }
                        }
                } catch (PlaywrightException pe) {
                        LOG.error("clearCheckBox() exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Clear Check Box. Detail: "
                                                        + pe.getMessage());
                }
                return page;
        }

        private Page navigateEditTerminationPage(Page page, String newHireDt) throws HirePlaywrightException {
                AutomationUtil automationUtil = new AutomationUtil();
                String dateFormat = environment.getProperty("com.argus.domain.util.date.format");
                String timezone = environment.getProperty("com.argus.domain.util.date.timezone");
                String modifyYearDateHire = "";

                Date currentHireDate = null;
                try {
                        currentHireDate = automationUtil.toDate(newHireDt, dateFormat);
                } catch (ParseException e) {
                        LOG.error(() -> "navigateEditTerminationPage() exception: " + e);
                        throw new HirePlaywrightException(e.getMessage());
                }

                Calendar calCurrentHireDate = automationUtil.dateToCalendar(currentHireDate, timezone);
                // Substract -1 day for resignation
                calCurrentHireDate.add(Calendar.DAY_OF_MONTH, -1);
                modifyYearDateHire = automationUtil.toDateString(calCurrentHireDate.getTime(), dateFormat, timezone);

                try {

                        // Edit Termination Box
                        playwrightAutomationService.pageLocatorWait(page, "div", "id",
                                        "'pt1:atkfr1:0:rQuick:3:up1Upl:UPsp1:WnwPse:PSEt1'").click();
                        // Inputbox
                        playwrightAutomationService.pageFill(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:3:up1Upl:UPsp1:WnwPse:Wnw2Rgn:1:iptrDt::content'",
                                        modifyYearDateHire);

                        page.mouse().move(100, 100);
                        // Input - box What's the way to terminate the employee
                        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("What's the way to terminate the employee?")).locator("a").click();
                        page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Hire")).getByText("Hire").click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);

                        // playwrightAutomationService.pageLocatorWait(page, "input", "id",
                        //                 "'pt1:atkfr1:0:rQuick:3:up1Upl:UPsp1:WnwPse:Wnw2Rgn:1:sis1:aidis::content'")
                        //                 .click();
                        // TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // page.keyboard().press("ArrowDown");
                        // TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // page.keyboard().press("ArrowDown");
                        // TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // page.keyboard().press("Enter");

                        // Input box - Why are you terminating <name>?
                        playwrightAutomationService.pageLocatorWait(page, "input", "id",
                                        "'pt1:atkfr1:0:rQuick:3:up1Upl:UPsp1:WnwPse:Wnw2Rgn:1:sis2:aridis::content'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("ArrowDown");
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        page.keyboard().press("Enter");

                        // playwrightAutomationService.pageLocatorWait(page, "input", "id",
                        // "'pt1:atkfr1:0:rQuick:3:up1Upl:UPsp1:gpRgn:0:GPmtfr0:0:sis2:aridis::content'")
                        // .focus();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                        // Submit
                        playwrightAutomationService
                                        .pageLocatorWait(page, "div", "id",
                                                        "'pt1:atkfr1:0:rQuick:3:up1Upl:UPsp1:SPsb2'")
                                        .click();
                        TimeUnit.SECONDS.sleep(SET_SHORTER_SEC_VALUE);
                } catch (PlaywrightException | InterruptedException pe) {
                        LOG.error("navigateEditTerminationPage() exception: " + pe);
                        playwrightAutomationService.screenshotSaveFile(page,
                                        environment.getProperty("argus.hire.folder.ss"));
                        throw new HirePlaywrightException(
                                        "There is something wrong during navigate the Edit Terminal Page. Detail: "
                                                        + pe.getMessage());
                }

                return page;
        }
}
