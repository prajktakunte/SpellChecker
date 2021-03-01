package com.prajktak.restapi.spellchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.prajktak.restapi.spellchecker.data.RefDictionary;

/*
 * Main class for spell checker service
 * @author Prajkta Kunte 
 */
@SpringBootApplication
@EnableCaching
public class SpellcheckerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpellcheckerServiceApplication.class, args);
	}

	@Bean(name = "RefDictionary")
	@Scope("singleton")
	public RefDictionary getReferenceDictionary() {
		return new RefDictionary();
	}

}
