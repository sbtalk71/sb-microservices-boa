package com.demo.spring;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hr")
public class HrController {

	private HrService hrService;
	
	public HrController(HrService hrService) {
		this.hrService = hrService;
	}


	@GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EmpDTO> getEmpInfo(@PathVariable Integer id){
		return ResponseEntity.ok(hrService.findEmp(id));
	}

	@GetMapping("test")
	public String testBalancer() {
		return hrService.testBalancer();
	}
	
}
