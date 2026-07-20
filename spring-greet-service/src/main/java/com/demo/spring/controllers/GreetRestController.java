package com.demo.spring.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.Emp;


@RestController
public class GreetRestController {

	@GetMapping(path="/greet/{name}", produces = MediaType.TEXT_PLAIN_VALUE)
	//GET http://localhost:8080/greet/Shantanu?country=India
	public String getGreet(@PathVariable("name") String userName,  @RequestParam(name="country") String country) {
		return "Hello from Spring, "+userName+" from "+country;
	}
	
	@GetMapping(path="/emp/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Emp findEmp(@PathVariable Integer id) {
		return new Emp(id, "Rajesh", "Hyderabad", 80000.0);
	}
}
