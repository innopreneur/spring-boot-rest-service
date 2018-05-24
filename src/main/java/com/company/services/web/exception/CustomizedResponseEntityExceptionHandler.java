package com.company.services.web.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler 
			extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public final ResponseEntity<Object> handleAllExceptions(
			Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse(new Date(), ex.getMessage(),
						request.getDescription(false));
		
		return new ResponseEntity<Object>(
				exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(
			EmployeeNotFoundException ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse(new Date(), ex.getMessage(),
						request.getDescription(false));
		
		return new ResponseEntity<Object>(
				exceptionResponse, HttpStatus.NOT_FOUND);
	}

	
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public final ResponseEntity<Object> handleBadRequestExceptions(
			Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse(new Date(), ex.getMessage(),
						request.getDescription(false));
		
		return new ResponseEntity<Object>(
				exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public final ResponseEntity<Object> handleAuthenticationExceptions(
			Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse(new Date(), ex.getMessage(),
						request.getDescription(false));
		
		return new ResponseEntity<Object>(
				exceptionResponse, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public final ResponseEntity<Object> handleUnauthorizedExceptions(
			Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse(new Date(), ex.getMessage(),
						request.getDescription(false));
		
		return new ResponseEntity<Object>(
				exceptionResponse, HttpStatus.FORBIDDEN);
	}

}