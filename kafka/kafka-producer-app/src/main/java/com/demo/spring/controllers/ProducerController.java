package com.demo.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.EmpDTO;

@RestController
public class ProducerController {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	@Qualifier("kafkaTemplateJson")
	private KafkaTemplate<String, EmpDTO> kafkaTemplateJson;
	
	@GetMapping(path = "/send/{message}")
	public String send(@PathVariable String message) {
		this.kafkaTemplate.send("demo-topic",message);
		return "message sent";
	}
	
	
	@GetMapping(path = "/send/emp")
	public String sendEmp() {
		this.kafkaTemplateJson.send("demo-topic",new EmpDTO(100, "Tarun", "Indore", 90000));
		return "message sent";
	}
}
