package com.prajktak.restapi.spellchecker.util;

/*
 * Class to represent custom exceptions
 * @author Prajkta Kunte 
 */
@SuppressWarnings("serial")
public class invalidWordException extends Exception{
	public invalidWordException(String message) {
		super(message);
	}

}
