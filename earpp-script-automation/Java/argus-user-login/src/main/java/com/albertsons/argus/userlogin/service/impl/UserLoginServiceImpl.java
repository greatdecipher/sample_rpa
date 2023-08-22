package com.albertsons.argus.userlogin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.exception.DomainException;
import com.albertsons.argus.domain.playwright.service.CitrixService;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.LoginSSOAutomationService;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.userlogin.model.User;
import com.albertsons.argus.userlogin.service.UserLoginService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    private static final Logger LOG = LogManager.getLogger(UserLoginServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private LoginSSOAutomationService loginSSOAutomationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CitrixService citrixService;

    @Autowired
    private PlaywrightAutomationService playwrightAutomationService;

    @Override
    public List<String> getLoginInfo() {
        // Get Data from KeyVault
        // JsonObject jsonCredentials = JsonParser.parseString(environment.getProperty("login-5f-credentials"))
        //         .getAsJsonObject();
        JsonObject jsonCredentials =
        JsonParser.parseString(environment.getProperty("test.string.credentials")).getAsJsonObject();
        String[] credentialsSplit = jsonCredentials.get("values").getAsString().split("\\^\\^\\^");
        List<String> userInfo = new ArrayList<String>(Arrays.asList(credentialsSplit));
        return userInfo;
    }

    @Override
    public void loginMultipleUserRestart(List<String> userInfo) throws Exception{
        User userProcessed = new User();
        for (String user : userInfo) {
            Browser browser = null;
            try {
                browser = playwrightAutomationService.openBrowser();
                BrowserContext browserContext = browser
                        .newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
                userProcessed = new User();
                String[] userSplit = user.split("\\|\\|\\||~~~");
                if (userSplit.length == 3) {
                    userProcessed.setUsername(userSplit[0]);
                    userProcessed.setPassword(userSplit[1]);
                    userProcessed.setSecretKey(userSplit[2]);
                } else {
                    userProcessed.setUsername(user.substring(0, 7));
                    userProcessed.setLoggedIn(false);
                    continue;
                }
                // Page page = loginSSOAutomationService.openBrowser();
                // try {
                Page page = citrixService.navigateCitrixSaasLogin(browser, browserContext, userProcessed.getSecretKey(),
                        userProcessed.getUsername() + "@safeway.com", userProcessed.getPassword());
                playwrightAutomationService.pageClick(page, "button", "data-testid", "detection-use_browser");
                playwrightAutomationService.pageClick(page, "span", "data-testid", "link-text-desktops");
                playwrightAutomationService.pageClick(page, "path", "fill-rule", "evenodd");
                playwrightAutomationService.pageClick(page, "div", "class", "wsui-dxua7e");
                playwrightAutomationService.pageClick(page, "button", "class", "wsui-1vj7szx");

                TimeUnit.SECONDS.sleep(240);
                // page = loginSSOAutomationService.loginSSO(page,
                // environment.getProperty("login.user.uri"), userProcessed.getUsername(),
                // userProcessed.getPassword(), userProcessed.getSecretKey());

                // } catch (Exception e) {
                // userProcessed.setLoggedIn(false);
                // usersProcessed.add(userProcessed);
                // LOG.error(e.getMessage());
                // }
            } catch (DomainException | InterruptedException e) {
                LOG.error(e);
                throw new Exception(e.getMessage());
            } finally {
                LOG.log(Level.DEBUG, () -> "closed browser. . .");
                // Closed Broswer
                playwrightAutomationService.closedBrowser(browser);
            }
        } 
    }

    @Override
    public String createMailTemplate(List<User> userProcessed) {
        StringBuilder sb = new StringBuilder();

        sb.append(environment.getProperty("login.mail.header"));
        sb.append("<table style=\"border: 1px\">");
        sb.append("<tr><th>USER</th><th>STATUS</th></tr>");
        if(!userProcessed.isEmpty()){
            for (User user : userProcessed) {
                sb.append("<tr>");
                sb.append("<td>");
                sb.append(user.getUsername());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(user.isLoggedIn() ? "OK" : "FAIL");
                sb.append("</td>");
                sb.append("</tr>");
    
            }
        }
        sb.append("</table>");
        LOG.info(sb.toString());
        return sb.toString();
    }

    @Override
    public String sendMail(String msg) {
        String[] to = { "RPA.CoE@albertsons.com" };
        try {
            emailService.sendSimpleMessage("bot@albertsons.com", "BOT_CITRIX", to, new String[]{"RPA.CoE@albertsons.com"}, "Restart Status", msg, 3, true);
        } catch (ArgusMailException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> loginMultipleUser(List<String> userInfo) throws Exception{
        List<User> usersProcessed = new ArrayList<>();

        User userProcessed = new User();
        for (String user : userInfo) {
            Browser browser = null;
            try {
                browser = playwrightAutomationService.openBrowser();
                BrowserContext browserContext = browser
                        .newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
                userProcessed = new User();
                String[] userSplit = user.split("\\|\\|\\||~~~");
                if (userSplit.length == 3) {
                    userProcessed.setUsername(userSplit[0]);
                    userProcessed.setPassword(userSplit[1]);
                    userProcessed.setSecretKey(userSplit[2]);
                } else {
                    userProcessed.setUsername(user.substring(0, 7));
                    userProcessed.setLoggedIn(false);
                    usersProcessed.add(userProcessed);
                    continue;
                }
                // Page page = loginSSOAutomationService.openBrowser();
                // try {
                Page page = citrixService.navigateCitrixSaasLogin(browser, browserContext, userProcessed.getSecretKey(),
                        userProcessed.getUsername() + "@safeway.com", userProcessed.getPassword());
                playwrightAutomationService.pageClick(page, "button", "data-testid", "detection-use_browser");
                playwrightAutomationService.pageClick(page, "span", "data-testid", "link-text-desktops");
                playwrightAutomationService.pageClick(page, "div", "class", "child_zdxht7");

                TimeUnit.SECONDS.sleep(600);
                // Thread.sleep(720000);
                
                userProcessed.setLoggedIn(true);
                usersProcessed.add(userProcessed);
            } catch (DomainException | InterruptedException e) {
                userProcessed.setLoggedIn(false);
                usersProcessed.add(userProcessed);

                LOG.error(e);
            } finally {
                LOG.log(Level.DEBUG, () -> "closed browser. . .");
                // Closed Broswer
                playwrightAutomationService.closedBrowser(browser);
            }
        } 
        return usersProcessed;
    }

}
