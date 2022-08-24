package com.tweetapp.cutomizedException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiException extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
		String errorMessageDes = ex.getLocalizedMessage();
		if (errorMessageDes == null) {
			errorMessageDes = ex.toString();
		}
		ErrorMessage errorMessage = new ErrorMessage(errorMessageDes);
		String customizedMessage = "Something Went Wrong ! Check Your input";
		return new ResponseEntity<>(customizedMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
