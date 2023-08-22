package com.albertsons.argus.mim;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("argus-app.properties")
@EncryptablePropertySource("argus-cred.properties")
public class MimAppConfig {

}
