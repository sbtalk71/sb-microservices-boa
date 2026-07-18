package com.demo.spring.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.entities.Emp;
import com.demo.spring.services.EmpService;

@RestController
@RequestMapping("/emp")
public class EmpRestController {

	private EmpService empService;

	public EmpRestController(EmpService empService) {
		this.empService = empService;
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Emp> getOneEmp(@PathVariable Integer id) {
		return ResponseEntity.ok(empService.findOneEmp(id));
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Emp>> listAllEmps(){
		return ResponseEntity.ok(empService.getAll());
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Emp> save( @RequestBody Emp emp){
		System.out.println("At POST controller entry empId : "+emp.getEmpId());
		return ResponseEntity.ok(empService.saveEmp(emp));
	}
	
	@PutMapping( path="/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Emp> update(@PathVariable Integer id,  @RequestBody Emp emp){
		
		return ResponseEntity.ok(empService.updateEmp(id,emp));
	}
}
