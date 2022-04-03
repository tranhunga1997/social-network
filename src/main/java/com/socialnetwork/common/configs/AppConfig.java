package com.socialnetwork.common.configs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.socialnetwork.common.utils.MessageUtils;

@Configuration
public class AppConfig {
	@Autowired
	ConfigurableApplicationContext context;
	@PostConstruct
	public void setMessageUtils() {
		MessageUtils.setAppContent(context);
	}
}
