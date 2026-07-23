package com.demo.spring;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hibernate.annotations.processing.Exclude;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.demo.spring.entities.Emp;
import com.demo.spring.repositories.EmpRepository;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmpServiceTest {

	@MockitoBean
	EmpRepository empRepository;
	
	@Autowired
	EmpService empService;
	
	@Test
	void shouldReturnEmp() {
		
		Emp emp= new Emp("kiran", "bangalore", 45000.0);
		when(empRepository.findById(1)).thenReturn(Optional.of(emp));
		
		Emp result=empService.findEmpById(1);
		Assertions.assertEquals("kiran", result.getName());
		
	}
}
