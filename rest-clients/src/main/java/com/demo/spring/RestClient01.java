package com.demo.spring;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

public class RestClient01 {

	public static void main(String[] args) {
		RestTemplate restTemplate=new RestTemplate();
		
		String response= restTemplate.getForObject("http://localhost:8181/emp/52", String.class);
		System.out.println(response);
		
		Emp emp= restTemplate.getForObject("http://localhost:8181/emp/52", Emp.class);
		System.out.println(emp.getName());
		
		
		RestClient restClient=RestClient.create();
		
	//RestClient.Builder clientBuilder=RestClient.builder();
		
	String data=	restClient.get()
		.uri("http://localhost:8181/emp/52")
		.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		.body(String.class);
	System.out.println(data);
	
	
	
	Emp emp1 = new Emp("Amit","Delhi",800000.0);
	 Emp responseEmp= restClient.post()
	 .uri("http://localhost:8181/emp")
	 .body(emp1)
	 .accept(MediaType.APPLICATION_JSON)
	 .contentType(MediaType.APPLICATION_JSON)
	 .retrieve()
	 .body(Emp.class);
	 
	 System.out.println(responseEmp.getEmpId());

	}

}
