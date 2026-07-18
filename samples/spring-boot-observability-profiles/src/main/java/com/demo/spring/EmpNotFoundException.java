package com.demo.spring;

public class EmpNotFoundException extends RuntimeException {

	public EmpNotFoundException() {

	}

	public EmpNotFoundException(String message) {
		super(message);
	}
}
