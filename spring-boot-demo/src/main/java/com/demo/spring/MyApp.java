package com.demo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MyApp {

	private NotifactionService notificaService;
	
	public MyApp(@Qualifier("emailNotification") NotifactionService notificaService) {
		this.notificaService = notificaService;
	}

		
	public void sendNotification() {
		notificaService.notifyChannel();
	}
}
