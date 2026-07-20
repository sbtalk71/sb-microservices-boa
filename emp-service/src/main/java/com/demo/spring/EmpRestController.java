package com.demo.spring;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.entities.Emp;
import com.demo.spring.repositories.EmpRepository;

@RestController
@RequestMapping("/emp")
public class EmpRestController {

	private Logger logger=LoggerFactory.getLogger(this.getClass().getName());
	private EmpRepository empRepository;
	
	
	public EmpRestController(EmpRepository empRepository) {
		logger.info("The class Name that spring creatred is {}", empRepository.getClass().getName());
		this.empRepository = empRepository;
	}


	@GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Emp> getAll(){
		return empRepository.findAll();
	}
	
	
}
