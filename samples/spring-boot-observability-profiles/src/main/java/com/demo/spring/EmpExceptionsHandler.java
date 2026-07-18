package com.demo.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.demo.spring.utils.ResponseMessage;

@RestControllerAdvice
public class EmpExceptionsHandler {

	@ExceptionHandler(EmpNotFoundException.class)
	public ResponseEntity<ResponseMessage> handleException(EmpNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(ex.getMessage()));
	}
	
	@ExceptionHandler(EmpExistsException.class)
	public ResponseEntity<ResponseMessage> handleException1(EmpExistsException ex){
		return ResponseEntity.ok(new ResponseMessage(ex.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseMessage> handleException2(MethodArgumentNotValidException ex){
		
		Map<String, String> errors=new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error->errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.ok(new ResponseMessage(errors.toString()));
	}
}
