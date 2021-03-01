package com.prajktak.restapi.spellchecker.util;

/*
 * Class to represent custom exceptions
 * @author Prajkta Kunte 
 */
@SuppressWarnings("serial")
public class wordNotFoundException  extends Exception {
	
	public wordNotFoundException(String word) {
		super("No spelling suggestions available for the word " + word);
	}
}
