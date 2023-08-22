package com.albertsons.argus.domain.service.impl;

import com.albertsons.argus.domain.bo.generated.ByQueueBO;
import com.albertsons.argus.domain.service.AutomationService;

import org.springframework.stereotype.Service;
/**
 * @author kbuen03
 * @version 1.0
 * @since 5/18/12
 * 
 */
@Service
public class ByQueueBoAutomationServiceImpl implements AutomationService<ByQueueBO>{
	// private static final Logger LOG = LogManager.getLogger(AutomationServiceImpl.class);
	@Override
	public Class<ByQueueBO> getTClass() {
		return ByQueueBO.class;
	}
     
}
