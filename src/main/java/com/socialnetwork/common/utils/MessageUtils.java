package com.socialnetwork.common.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;

import com.socialnetwork.SocialNetworkApplication;

public class MessageUtils {
	private static MessageSource messageSource=null;
	private static ApplicationContext context;
	public static void setAppContent(ApplicationContext context) {
		MessageUtils.context = context;
	}
	public static String getMessage(String errorCode, Object...args) {
		if(messageSource==null) {
			//ApplicationContext context = SocialNetworkApplication.getAppContext();
			messageSource = context.getBean(MessageSource.class);
		}
		return messageSource.getMessage(errorCode, args, Locale.ROOT);
	}
}
