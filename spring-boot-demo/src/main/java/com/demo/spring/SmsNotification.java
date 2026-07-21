package com.demo.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class SmsNotification implements NotifactionService {

	private Logger logger=LoggerFactory.getLogger(this.getClass().getName());
	
	 public SmsNotification() {
		System.out.println("SMS Notification Object created..");
	}
	@Override
	public void notifyChannel() {
		

		logger.info("SMS notification sent..");
	}

}
