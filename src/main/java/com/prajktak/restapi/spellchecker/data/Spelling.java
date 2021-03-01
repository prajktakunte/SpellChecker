package com.prajktak.restapi.spellchecker.data;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prajktak.restapi.spellchecker.util.SpellMatcher;
import com.prajktak.restapi.spellchecker.util.wordNotFoundException;


/*
 * Class to represent the spelling service response object.  
 * @author Prajkta Kunte 
 */
@Component
public class Spelling {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Spelling.class);

	private boolean correct;
	private List<String> suggestions;

	@Autowired
	private SpellMatcher speller;

	public boolean isCorrect() {
		return correct;
	}

	public List<String> getSuggestions() {
		return suggestions;
	}

	public Spelling() {
		this.correct = true;
		this.suggestions = null;
	}

	public Spelling(boolean correct, List<String> suggestions) {
		this.correct = correct;
		this.suggestions = suggestions;
	}

	public Spelling getSpelling(String word) throws wordNotFoundException {
		try {
			Spelling s = null;
			if (word != null && !word.isBlank()) {				

				if (speller.isCorrectSpelling(word)) {
					logger.debug("Found correct match");
					s = new Spelling(true, null);
				} else {

					logger.debug("Find word suggestions");
					s = new Spelling(false, new ArrayList<String>());
					List<String> wordList = speller.findSuggestions(word);
					if(wordList != null) {
						Collections.sort(wordList);
						s.getSuggestions().addAll(wordList);
					}
					else {
						throw new wordNotFoundException(word);
					}
				}
			}
			return s;
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
			throw new wordNotFoundException(word);
		}

	}

}
