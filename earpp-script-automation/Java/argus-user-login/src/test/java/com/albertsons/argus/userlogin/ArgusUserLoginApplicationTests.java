package com.albertsons.argus.userlogin;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.albertsons.argus.userlogin.model.User;
import com.albertsons.argus.userlogin.service.UserLoginService;

@SpringBootTest
@Disabled
class ArgusUserLoginApplicationTests {

	@Autowired
	private UserLoginService userLoginService;

	@Test
	void contextLoads() {
		
	}

}
