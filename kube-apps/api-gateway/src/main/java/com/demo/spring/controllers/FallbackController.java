package com.demo.spring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

	@GetMapping("/hr-fallback")
	public String hrFallback() {
		return "hr-service down, try after a while";
	}
}
