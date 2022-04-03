package com.socialnetwork.common.utils;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.socialnetwork.SocialNetworkApplication;

public class MessageUtils {
	private static MessageSource messageSource=null;
	public static String getMessage(String errorCode, Object...args) {
		if(messageSource==null) {
			ApplicationContext context = SocialNetworkApplication.getAppContext();
			messageSource = context.getBean(MessageSource.class);
		}
		return messageSource.getMessage(errorCode, args, Locale.ROOT);
	}
}
