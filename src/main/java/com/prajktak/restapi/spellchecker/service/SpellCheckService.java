package com.prajktak.restapi.spellchecker.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.prajktak.restapi.spellchecker.data.Spelling;
import com.prajktak.restapi.spellchecker.util.invalidWordException;
import com.prajktak.restapi.spellchecker.util.wordNotFoundException;
import com.prajktak.restapi.spellchecker.validator.SpellCheckValidator;

/*
 * Spell checker service class
 * @author Prajkta Kunte 
 */
@Service
public class SpellCheckService {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SpellCheckService.class); 
	
	@Autowired
	private Spelling s;
	
	@Autowired
	private SpellCheckValidator validator;

	@Cacheable("spellSuggestions")
	public Spelling getSpelling(String word) throws invalidWordException, wordNotFoundException{
		try {			
			logger.debug("Calling getSpelling");
			
			logger.debug("Validate word " + word);
			validator.validateWord(word);
			
			logger.debug("Check spelling " + word);
			return s.getSpelling(word);
			
		} catch (Exception ex) {
			throw ex;
		}

	}
}
