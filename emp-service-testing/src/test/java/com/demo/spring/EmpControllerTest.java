package com.demo.spring;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.spring.controllers.EmpRestController;
import com.demo.spring.entities.Emp;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmpRestController.class)
public class EmpControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	
	@MockitoBean
	EmpService empService;
	
	@Test
	void shouldResturnEmp() throws Exception{
		Emp emp=new Emp("Shantanu", "Hyderabad", 90000.0);
		emp.setEmpId(2);
		
		when(empService.findEmpById(2)).thenReturn(emp);
		
		mockMvc.perform(get("/emp/2"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("Shantanu"));
		
	}
	
	@Test
	void shouldResturnEmpList() throws Exception{
		Emp emp=new Emp("Shantanu", "Hyderabad", 90000.0);
		emp.setEmpId(2);
		
		List<Emp> empList=new ArrayList<>();
		empList.add(emp);
		
		when(empService.listAllEmps()).thenReturn(empList);
		
		mockMvc.perform(get("/emp"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.length()").value("1"))
		.andExpect(jsonPath("$[0].name").value("Shantanu"));
		
	}
	
	@Test
	void shouldSaveEmp() throws Exception{
		Emp emp=new Emp("Shantanu", "Hyderabad", 90000.0);
		//emp.setEmpId(2);
		
		ObjectMapper mapper=new ObjectMapper();
		String jsonData=mapper.writeValueAsString(emp);
		
		when(empService.save(emp)).thenReturn(emp);
		
		mockMvc.perform(post("/emp").content(jsonData).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		//.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		//.andExpect(jsonPath("$.name").value("Shantanu"));
		
	}
}
