package com.albertsons.argusautomationlexmarkorder.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.playwright.service.impl.PlaywrightAutomationServiceImpl;

import com.albertsons.argusautomationlexmarkorder.services.AutomationProcessService;
import com.albertsons.argusautomationlexmarkorder.services.LexmarkAutomationService;
import com.albertsons.argusautomationlexmarkorder.services.impl.AutomationProcessServiceImpl;
import com.albertsons.argusautomationlexmarkorder.services.impl.LexmarkAutomationServiceImpl;

@Configuration
@Profile({"local","dev","qa","prod"})
@PropertySources(
    @PropertySource(value = "classpath:argus-lexmark-${spring.profiles.active}.properties"))
public class LexmarkAppConfig {
	@Bean
    public LexmarkAutomationService lexmarkAutomationService(){
        return new LexmarkAutomationServiceImpl();
    }
	@Bean
	public AutomationProcessService automationProcessService() {
		return new AutomationProcessServiceImpl();
	}

	@Bean
	public PlaywrightAutomationService playwrightAutomationService(){
		return new PlaywrightAutomationServiceImpl();
	}
  
}
