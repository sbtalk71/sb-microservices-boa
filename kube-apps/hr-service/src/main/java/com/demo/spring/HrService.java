package com.demo.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient.Builder;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class HrService {

	private Builder clientBuilder;
	
	@Value("${emp.service.uri}")
	private String targetUri;

	public HrService(Builder clientBuilder) {
		this.clientBuilder = clientBuilder;
	}

	@CircuitBreaker(name="hr-service-cb", fallbackMethod = "findEmpFallback")
	public EmpDTO findEmp(Integer id) {

		return clientBuilder.build()
				.get().uri(targetUri+"/emp/"+ + id)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve().body(EmpDTO.class);
	}
	
	
	public EmpDTO findEmpFallback(Throwable t) {
		//"Service down, comeback after a while";
		return new EmpDTO("no data", "No data", 0.0);
	}
	public String testBalancer() {

		return clientBuilder.build()
				.get().uri(targetUri+"/balancer-test")
				.retrieve().body(String.class);
	}
}
