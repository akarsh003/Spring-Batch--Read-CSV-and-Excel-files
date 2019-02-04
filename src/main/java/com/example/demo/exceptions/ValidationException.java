package com.example.demo.exceptions;

public class ValidationException extends Exception {

	private final String attributeName;

	// Constructor that accepts a message
	public ValidationException(String message, String attributeName) {
		super(message);
		this.attributeName = attributeName;
		//System.out.println(message+" "+attributeName);
	}

	public String getAttributeName() {
		return attributeName;
	}
}
