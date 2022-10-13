package com.cgi.grocery.exception;

public class APIException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public APIException(String resourceName, String message) {
		super(String.format("%s not found with %s", resourceName, message)); 
		
	}
	public APIException(String message) {
		super(String.format(message)); 
	}

}
