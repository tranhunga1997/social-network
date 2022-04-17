package com.socialnetwork.common.configs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import com.socialnetwork.common.utils.MailUtil;
import com.socialnetwork.common.utils.MessageUtils;

@Configuration
public class AppConfig {
	@Autowired
	ConfigurableApplicationContext context;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    
	@PostConstruct
	public void setMessageUtils() {
		MessageUtils.setAppContent(context);
	}
	
	@PostConstruct
	public void initMailService() {
		MailUtil.setJavaMailSender(javaMailSender);
		MailUtil.setJavaMailSender(javaMailSender);
	}
}
