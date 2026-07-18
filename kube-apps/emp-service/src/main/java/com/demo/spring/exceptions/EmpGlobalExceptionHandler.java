package com.demo.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.demo.spring.utils.MessageData;

@RestControllerAdvice
public class EmpGlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<MessageData> handle(RuntimeException ex){
		return ResponseEntity.ok(new MessageData(ex.getMessage()));
	}
	
	@ExceptionHandler(EmpNotFoundException.class)
	public ResponseEntity<MessageData> handle(EmpNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageData(ex.getMessage()));
	}
}
