package com.demo.spring.exceptions;

public class ResourceException extends RuntimeException {

	public ResourceException() {
		super();
	}

	public ResourceException(String message) {
		super(message);
	}
}
