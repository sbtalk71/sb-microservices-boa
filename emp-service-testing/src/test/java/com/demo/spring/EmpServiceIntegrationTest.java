package com.demo.spring;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.spring.entities.Emp;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmpServiceIntegrationTest {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	void shouldResturnEmp() throws Exception{
			
		mockMvc.perform(get("/emp/52"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("Rekha"));
		
	}
	
}
