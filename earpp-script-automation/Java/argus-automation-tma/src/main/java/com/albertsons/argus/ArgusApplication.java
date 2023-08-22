package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @author kbuen03
 * @version 1.0
 * @since 5/14
 * 
 */
@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.tma", "com.albertsons.argus.domain",
"com.albertsons.argus.mail","com.albertsons.argus.webservice" })
@EnableScheduling
public class ArgusApplication {
    private static final Logger LOG = LogManager.getLogger(ArgusApplication.class);
    // @Autowired
    // private TMAAutomationService tmaAutomationService;

    // @Autowired
    // private AutomationService<ByQueueBO> automationService;

    // @Autowired
    // private AutomationService<ByProjectBO> tmaProjectService;

    public static void main(String[] args) {
        SpringApplication.run(ArgusApplication.class);
        // ConfigurableApplicationContext context = SpringApplication.run(ArgusApplication.class, args);
        LOG.info("Springboot Scheduler job enabled. . .");
        // System.exit(SpringApplication.exit(context, () -> 0));
    }

  /*   @Override
    public void run(String... args) throws Exception {
        List<ByQueueBO> tmaBOs = new ArrayList<>();
        List<String> headers = tmaAutomationService.getHeadersList();
        List<String> getContentLists = new ArrayList<>();

        try {
            Page pageTMA = tmaAutomationService.navigateLoginNonSSL();

            String pageTMAbyProject = tmaAutomationService.getbyProjectName(pageTMA);

            LOG.log(Level.DEBUG, () -> "pageTMAbyProject: " + pageTMAbyProject);

            ByProjectBO tmaProject = tmaProjectService.toJson(pageTMAbyProject);

            List<String> qManagersLists = tmaAutomationService.getQMManagers(tmaProject);

            getContentLists = tmaAutomationService.getContentLists(pageTMA, qManagersLists);
        } catch (ArgusTmaException e) {
            throw new ArgusTmaRuntimeException(e.getMessage());
        }

        tmaBOs = automationService.getToJsonLists(getContentLists);

        tmaAutomationService.sendTmaEmail(headers, tmaBOs);
    } */
}
