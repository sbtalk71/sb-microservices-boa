package com.demo.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient.Builder;

@Service
public class HrService {
	
	private Logger logger=LoggerFactory.getLogger(this.getClass().getName());
	@Value("${emp.service.url}")
	private String serviceUrl;

	private Builder restClientBuilder;

	public HrService(Builder restClientBuilder) {
		this.restClientBuilder = restClientBuilder;
	}
	
	
	public EmpDTO getEmpDetails(Integer id) {
		
		logger.info("connecting to backend to get emp Details with id {}",id);
		return restClientBuilder.build()
		.get()
		.uri(serviceUrl+"/emp/"+id)
		.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		.body(EmpDTO.class);
	}
}
