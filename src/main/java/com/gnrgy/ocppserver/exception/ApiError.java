package com.gnrgy.ocppserver.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class ApiError {

	private Boolean error;
	//@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
	//private LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private List<ApiSubError> subErrors;

	private ApiError() {
		//timestamp = LocalDateTime.now();
		this.error=true;
	}

	ApiError(Boolean error) {
		this();
		this.error = error;
	}

	ApiError(Boolean error, Throwable ex) {
		this();
		this.error = error;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	ApiError(Boolean error, String message, Throwable ex) {
		this();
		this.error = error;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}
}
