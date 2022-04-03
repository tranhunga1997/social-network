package com.socialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SocialNetworkApplication {
	private static ConfigurableApplicationContext context;
	
	public static ConfigurableApplicationContext getAppContext() {
		return context;
	}
	public static void main(String[] args) {
		context = SpringApplication.run(SocialNetworkApplication.class, args);
	}

}
