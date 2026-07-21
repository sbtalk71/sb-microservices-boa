package com.demo.spring;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MyApp {

	private NotifactionService notificaService;
	
	public MyApp(NotifactionService notificaService) {
		this.notificaService = notificaService;
	}

		
	public void sendNotification() {
		notificaService.notifyChannel();
	}
}
