package com.demo.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.demo.spring.entities.Emp;
import com.demo.spring.repositories.EmpRepository;

@DataJpaTest
public class EmpDataJpaTest {

	@Autowired
	EmpRepository empRepository;
	
	@Test
	void shouldSaveEmp() {
	Emp emp = new Emp("Shantanu", "Hyderabad", 56000.0);
	
	Emp returnedEmp=empRepository.save(emp);
	
	Assertions.assertEquals("Shantanu", returnedEmp.getName());
	}
}
