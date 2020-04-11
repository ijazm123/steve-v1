package com.gnrgy.ocppserver.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = "com.gnrgy.ocppserver.web.controller")
public class RestControllerAdvice {
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleAllException(Exception ex, HttpServletRequest request) {
		log.error("Request: {} raised following exception.", request.getRequestURL(), ex);
		return new ResponseEntity<>(new ApiError(true, "error", ex), HttpStatus.OK);
	}
}
