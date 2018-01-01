package com.ted.auctionbay.jpautils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.core.SpringVersion;

public class EntityManagerFactoryListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		System.out.println("CONTEXT INITIALIZED");
		System.out.println("version: " + SpringVersion.getVersion());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		/* Close the Entity manager when the context is destroyed */
		EntityManagerHelper.closeEntityManagerFactory();
	}

}
