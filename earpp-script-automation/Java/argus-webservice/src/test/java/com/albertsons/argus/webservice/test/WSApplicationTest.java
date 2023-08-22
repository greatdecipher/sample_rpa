package com.albertsons.argus.webservice.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.JsonService;
import com.albertsons.argus.webservice.service.MetricsWebService;

@SpringBootTest(classes = WSApplicationTest.class)
@ActiveProfiles("test")
public class WSApplicationTest {
    @MockBean
	private MetricsWebService metricsWebService;

	@MockBean
	private JsonService<ResponseStartSaveBO> jsonService;

	@MockBean
	private JsonService<ResponseIncrementTransactionBO> jsonService2;

	@MockBean
	private JsonService<ResponseEndSaveBO> jsonService3;

	@Test
	void contextLoads() {
		assertNotNull(metricsWebService);
		assertNotNull(jsonService);
		assertNotNull(jsonService2);
		assertNotNull(jsonService3);
	}
}
