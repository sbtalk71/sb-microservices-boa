package com.demo.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyAppRunner implements CommandLineRunner {

	private MyApp myapp;
	
	public MyAppRunner(MyApp myapp) {
		this.myapp = myapp;
	}

	@Override
	public void run(String... args) throws Exception {
		myapp.sendNotification();
		
	}

}
