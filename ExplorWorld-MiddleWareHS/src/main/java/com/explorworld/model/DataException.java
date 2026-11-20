package com.explorworld.model;

public class DataException extends ExplorWorldException {

	// Constructor para mensajes de error lógicos de datos
	public DataException(String message) {
		super(message);
	}
	
	public DataException(Throwable cause) {
		super(cause);
	}

	// Constructor que recibe un mensaje personalizado y la SQLException
	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

}
