package com.albertsons.argus.domain.service.impl;

import com.albertsons.argus.domain.bo.generated.ByProjectBO;
import com.albertsons.argus.domain.service.AutomationService;

import org.springframework.stereotype.Service;
/**
 * @author kbuen03
 * @version 1.0
 * @since 5/18/12
 * 
 */
@Service
public class ByProjectBoAutomationServiceImpl implements AutomationService<ByProjectBO>{
	// private static final Logger LOG = LogManager.getLogger(TmaProjectAutomationServiceImpl.class);
    @Override
    public Class<ByProjectBO> getTClass() {

        return ByProjectBO.class;
    }
    
}
