package com.prajktak.restapi.spellchecker.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.prajktak.restapi.spellchecker.util.invalidWordException;

/*
 * Class to validate user input
 * @author Prajkta Kunte
 */
@Component
public class SpellCheckValidator {
	
	@Value("${spring.application.user-input-max-length:45}")
	int maxLen;
	
	public void validateWord(String word) throws invalidWordException{
		/*Word is considered valid if:
		  it is not null 
		  it is not empty
		  it is not more than max allowed length
		  it contains only alphabets
		  */
		if (word == null || word.isBlank() || word.length() > maxLen || !word.matches("[a-zA-Z]+"))
			throw new invalidWordException("Validation check failed for " + word);
	}
	
	
}
