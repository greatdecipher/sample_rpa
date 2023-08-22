package com.albertsons.argus.tma.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.albertsons.argus.domain.bo.generated.ByProjectBO;
import com.albertsons.argus.domain.bo.generated.ByQueueBO;
import com.albertsons.argus.domain.bo.generated.TmaItem;
import com.albertsons.argus.domain.dto.QueueDTO;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.tma.exception.ArgusTmaException;
import com.albertsons.argus.tma.service.TMAAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author kbuen03
 * @version 1.0
 * @since 5/18/12
 * 
 */
@Service
public class TMAAutomationServiceImpl implements TMAAutomationService {
    private static final Logger LOG = LogManager.getLogger(TMAAutomationServiceImpl.class);
    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Override
    public String getContent(Page page, String navigateFilter) {
        LOG.log(Level.DEBUG, () -> "start getContent method. . .");
        page.navigate(navigateFilter);

        LOG.log(Level.DEBUG, () -> "end getContent method. . .");

        return page.textContent(TO_JSON_STR);
    }

    @Override
    public List<String> getContentLists(Page page, List<String> qmManagerLists) throws ArgusTmaException {
        LOG.log(Level.DEBUG, () -> "start getContentLists method. . .");
        List<String> contentLists = new ArrayList<>();

        double timeOut = Double.valueOf(environment.getProperty("playwright.tma.timeout.value"));

        page.setDefaultTimeout(timeOut);
        
        if (!qmManagerLists.isEmpty()) {
            for (String qmManger : qmManagerLists) {
                try {
                    page.navigate(environment.getProperty(PLAYWRIGHT_URI)
                            + environment.getProperty("playwright.uri.tma.local.queue.1") + qmManger
                            + environment.getProperty("playwright.uri.tma.local.queue.2")
                            + environment.getProperty("com.argus.tma.queue.name.filter")
                            + environment.getProperty("playwright.uri.tma.local.queue.3")
                            + environment.getProperty("com.argus.tma.queue.project"));
                } catch (PlaywrightException pw) {
                    throw new ArgusTmaException(pw.getMessage());
                }
                contentLists.add(page.textContent(TO_JSON_STR));
            }
        }
        LOG.log(Level.DEBUG, () -> "end getContentLists method. . .");

        return contentLists;
    }

    @Override
    public Page navigateLoginNonSSL(Browser browser) throws ArgusTmaException {
        LOG.log(Level.DEBUG, () -> "start navigateLoginNonSSL method. . .");
        Page page = null;

        try {
            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
           
            page = context.newPage();

            page.navigate(
                    environment.getProperty(PLAYWRIGHT_URI) + environment.getProperty("playwright.uri.tma.login"));

            page.fill("input[name=j_username]", environment.getProperty("encrypted.tma.property.username"));
            page.fill("input[name=j_password]", environment.getProperty("encrypted.tma.property.password"));

            page.click("button[id=loginButton]");

            LOG.log(Level.DEBUG, () -> "end navigateLoginNonSSL method. . .");
        } catch (PlaywrightException pw) {
            throw new ArgusTmaException(pw.getMessage());
        }

        return page;
    }

    @Override
    public String getbyProjectName(Page page) throws ArgusTmaException {
        LOG.log(Level.DEBUG, () -> "start getbyProjectName method. . .");
        String jsonStr = "";
        // com.argus.tma.queue.project - Project name
        page.navigate(environment.getProperty(PLAYWRIGHT_URI) + environment.getProperty("playwright.uri.tma.project")
                + environment.getProperty("com.argus.tma.queue.project"));

        jsonStr = page.textContent(TO_JSON_STR);

        if (jsonStr.contains("Unauthorized")) {
            throw new ArgusTmaException(jsonStr);
        } else if (jsonStr.trim().isEmpty()) {
            throw new ArgusTmaException("The text content is empty. Please check with the support team.");
        }
        LOG.log(Level.DEBUG, () -> "end getbyProjectName method. . .");

        return jsonStr;
    }

    @Override
    public List<String> getQMManagers(ByProjectBO project) {
        LOG.log(Level.DEBUG, () -> "start getQMManagers method. . .");
        List<String> lists = new ArrayList<>();

        String searchFilterQM = environment.getProperty("com.argus.tma.queue.manager.filter");

        if (searchFilterQM != null && searchFilterQM.length() > 0) {
            lists = project.getItems().stream()
                    .flatMap(o -> o.getName().contains(searchFilterQM) ? Stream.of(o.getName()) : Stream.empty())
                    .collect(Collectors.toList());
        } else {
            lists = project.getItems().stream().flatMap(o -> Stream.of(o.getName())).collect(Collectors.toList());
        }

        LOG.log(Level.DEBUG, () -> "end getQMManagers method. . .");

        return lists;
    }

    @Override
    public void sendTmaEmail(List<String> headers, List<ByQueueBO> tmaBOs) {
        LOG.log(Level.DEBUG, () -> "start sendTmaEmail method. . .");
        AutomationUtil util = new AutomationUtil();

        List<QueueDTO> queueDTOs = getQueuesBySort(tmaBOs);

        String body = util.toTmaHtmlString(headers, queueDTOs,
                environment.getProperty("com.argus.tma.queue.depth", Integer.class),
                environment.getProperty("com.argus.tma.queue.is.view.zero.depth", Boolean.class));
        try {
            if (body.contains("<td class=\"tg-hmp3\">")) {
                emailService.sendSimpleMessage(environment.getProperty("mail.esed.from"),
                        environment.getProperty("mail.esed.from.alias"),
                        environment.getProperty("mail.esed.high.priority.recipients", String[].class),
                        environment.getProperty("mail.esed.cc", String[].class),
                        environment.getProperty("mail.esed.high.priority.subject") + " - "
                                + util.toDateString(new Date(), environment.getProperty("domain.util.date.format"),"US/Arizona"), //TODO: must trasfer to property files
                        body, HIGH_PRIORITY,true);
            } else {
                emailService.sendSimpleMessage(environment.getProperty("mail.esed.from"),
                        environment.getProperty("mail.esed.from.alias"),
                        environment.getProperty("mail.esed.recipients", String[].class),
                        environment.getProperty("mail.esed.cc", String[].class),
                        environment.getProperty("mail.esed.subject") + " - "
                                + util.toDateString(new Date(), environment.getProperty("domain.util.date.format"),"US/Arizona"), //TODO: must trasfer to property files
                        body, NORMAL_PRIORITY,true);
            }

        } catch (ArgusMailException e) {
            // TODO: Implement
        }

        LOG.log(Level.DEBUG, () -> "end sendTmaEmail method. . .");

    }

    @Override
    public List<String> getHeadersList() {
        String[] getHeaderArr = environment.getProperty("mail.esed.headers", String[].class);
        List<String> getHeadersList = Arrays.asList(getHeaderArr);
        return getHeadersList;
    }

    private List<QueueDTO> getQueuesBySort(List<ByQueueBO> tmaBOs) {
        List<QueueDTO> queueDTOList = new ArrayList<>();
        QueueDTO queueDTO = null;

        for (ByQueueBO obj : tmaBOs) {
            if (obj.getItems() != null && !obj.getItems().isEmpty()) {
                for (TmaItem item : obj.getItems()) {
                    queueDTO = new QueueDTO();

                    queueDTO.setConnection(item.getWMQConnection());
                    queueDTO.setCurrentQueueDepth(item.getCurrentQDepth());
                    queueDTO.setMaxQueueDepth(item.getMaxQDepth());
                    queueDTO.setQueueName(item.getQName());
                    queueDTO.setReader(item.getOpenInputCount());
                    queueDTO.setWriter(item.getOpenOutputCount());

                    queueDTOList.add(queueDTO);
                }
            }
        }
        queueDTOList.sort((QueueDTO o1, QueueDTO o2) -> o2.getCurrentQueueDepth().compareTo(o1.getCurrentQueueDepth()));

        return queueDTOList;
    }

}
