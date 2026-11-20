package com.explorworld.model;

import com.explorworld.service.ServiceException;

public class ContentException extends ServiceException {
	
	public ContentException(String message) {
		super(message);
	}

	
	public ContentException(String message, Throwable cause) {
		super(message, cause);
	}
}
