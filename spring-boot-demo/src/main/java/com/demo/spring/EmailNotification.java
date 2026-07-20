package com.demo.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailNotification implements NotifactionService {

	private Logger logger=LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public void notifyChannel() {
		

		logger.info("Email notification sent..");
	}

}
