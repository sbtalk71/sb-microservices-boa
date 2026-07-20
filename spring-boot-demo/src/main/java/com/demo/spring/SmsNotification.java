package com.demo.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SmsNotification implements NotifactionService {

	private Logger logger=LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public void notifyChannel() {
		

		logger.info("SMS notification sent..");
	}

}
