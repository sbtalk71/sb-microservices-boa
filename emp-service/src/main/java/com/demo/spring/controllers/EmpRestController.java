package com.demo.spring.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.EmpService;
import com.demo.spring.entities.Emp;

@RestController
@RequestMapping("/emp")
public class EmpRestController {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private EmpService empService;

	public EmpRestController(EmpService empService) {
		this.empService = empService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Emp>> getAll() {
		return ResponseEntity.ok(empService.listAllEmps());
	}
	
	@GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Emp> findEmp(@PathVariable Integer id){
		
		Emp e=empService.findEmpById(id);
		return ResponseEntity.ok(e);
	}

}
