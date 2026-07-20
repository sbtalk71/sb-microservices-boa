package com.demo.spring;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DemoKafkaListener {

	//@KafkaListener(topics = "demo-topic",groupId = "demo-consumer-group")
	public void onMessage(String message) {
		System.out.println("Received : "+message);
	}
	
	@KafkaListener(topics = "demo-topic",groupId = "demo-consumer-group")
	public void onEmpMessage(EmpDTO message) {
		System.out.println("Received : "+message);
	}
}
