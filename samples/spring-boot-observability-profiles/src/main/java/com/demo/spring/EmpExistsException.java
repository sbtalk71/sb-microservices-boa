package com.demo.spring;

public class EmpExistsException extends RuntimeException {

	public EmpExistsException() {

	}

	public EmpExistsException(String message) {
		super(message);
	}
}
