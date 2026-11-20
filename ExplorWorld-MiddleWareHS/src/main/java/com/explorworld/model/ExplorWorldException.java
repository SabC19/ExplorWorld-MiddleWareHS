package com.explorworld.model;

public class ExplorWorldException extends Exception {

	public ExplorWorldException(String message) {
		super(message); 
	}
	
	public ExplorWorldException(Throwable cause) { 
		super(cause); 
	}
	
	public ExplorWorldException(String message, Throwable cause) { 
		super(message, cause); 
	}

}
