package com.prajktak.restapi.spellcheckerservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.prajktak.restapi.spellchecker.SpellcheckerServiceApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = SpellcheckerServiceApplication.class)
@ContextConfiguration
class SpellcheckerServiceApplicationTests {

    @Autowired
    private MockMvc mvc;
    
	@Test
	void contextLoads() throws Exception {
		
		mvc.perform(get("/spelling/House")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(200)).andDo(print())
			      .andExpect(content().string(containsString("true")));
	}
	
	@Test
	void spellingWothMixedCase() throws Exception {		
		mvc.perform(get("/spelling/BAllOOn")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(200)).andDo(print())
			      .andExpect(content().string(containsString("true")));
	}
	
	@Test
	void invalidInput() throws Exception {
		
		mvc.perform(get("/spelling/123")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(404)).andDo(print());
	}	
	
	@Test
	void  spellingWithRepeats() throws Exception {
		
		mvc.perform(get("/spelling/balllooooon")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(200)).andDo(print())
			      .andExpect(content().string(containsString("false")));		
		
			mvc.perform(get("/spelling/Balllon")
				      .contentType(MediaType.APPLICATION_JSON))
				      .andExpect(status().is(200)).andDo(print())
				      .andExpect(content().string(containsString("false")));			
	}	
	
	@Test
	void  spellingWithMissingVowels() throws Exception {
		
		mvc.perform(get("/spelling/balln")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(200)).andDo(print())
			      .andExpect(content().string(containsString("false")));
		
		mvc.perform(get("/spelling/bllllLLlln")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(200)).andDo(print())
			      .andExpect(content().string(containsString("false")));
	}
	
	@Test
	void  spellingforRandomText() throws Exception {
		
		mvc.perform(get("/spelling/dua")
			      .contentType(MediaType.APPLICATION_JSON))
				  .andExpect(status().is(404)).andDo(print());
		
		mvc.perform(get("/spelling/fsiuyfiusyifys")
			      .contentType(MediaType.APPLICATION_JSON))
		 		  .andExpect(status().is(404)).andDo(print());
	}		
	
}
