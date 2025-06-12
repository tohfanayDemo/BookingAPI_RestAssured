package com.qa.Booking.frameworkException;

public class APIFrameworkException extends RuntimeException{ //this is coming from java.lang package
	

	public APIFrameworkException(String msg) {//pass a message that you really want to throw along with exception
		
		//i want to call my parent class constructor and need super keyword for it:
		super(msg);
		
		
	}
}
