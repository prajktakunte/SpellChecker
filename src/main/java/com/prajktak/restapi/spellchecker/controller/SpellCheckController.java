package com.prajktak.restapi.spellchecker.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prajktak.restapi.spellchecker.data.Spelling;
import com.prajktak.restapi.spellchecker.service.SpellCheckService;
import com.prajktak.restapi.spellchecker.util.invalidWordException;
import com.prajktak.restapi.spellchecker.util.wordNotFoundException;

/*
 * Rest controller class for spell checker service
 * @author Prajkta Kunte 
 */
@RestController
public class SpellCheckController {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SpellCheckController.class);

	@Autowired
	private SpellCheckService scService;

	@GetMapping(path = "/spelling/{word}", produces = "application/json")
	public ResponseEntity<?> getSpelling(@PathVariable(value = "word", required = true) String word) {
		try {
			logger.info("Request for word " + word);
			Spelling s = scService.getSpelling(word);
			ResponseEntity<Spelling> responseEntity = new ResponseEntity<Spelling>(s, HttpStatus.OK);
			return responseEntity;
		} catch (Exception ex) {
			if (ex instanceof wordNotFoundException) {
				logger.info(ex.getMessage());
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			} else if (ex instanceof invalidWordException) {
				logger.info(ex.getMessage());
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
}
