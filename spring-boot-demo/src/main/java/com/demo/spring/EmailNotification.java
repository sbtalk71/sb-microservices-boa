package com.demo.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class EmailNotification implements NotifactionService {

	private Logger logger=LoggerFactory.getLogger(this.getClass().getName());
	
	public EmailNotification() {
		System.out.println("Email Notification Object created..");
	}
	@Override
	public void notifyChannel() {
		

		logger.info("Email notification sent..");
	}

}
