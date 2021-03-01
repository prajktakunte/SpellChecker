package com.prajktak.restapi.spellchecker.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;

/*
 * Class to load the reference dictionary from the URL specified in the configuration. 
 * This dictionary will be used to check the spelling of the user entered word. 
 * The URL for the dictionary is configurable.
 * @author Prajkta Kunte 
 */
public class RefDictionary {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RefDictionary.class);

	private HashSet<String> dictionary;

	@Value("${spring.application.dictionary.url:https://dbl-interview.dreambox.dev}")
	private String dictionaryURL;

	@Value("${spring.application.dictionary.filename:wordsEn}")
	private String dictionaryName;

	@Autowired
	ResourceLoader resourceLoader;

	public String getDictionaryURL() {
		return this.dictionaryURL;
	}

	public String getDictionaryName() {
		return this.dictionaryName;
	}

	public HashSet<String> getDictionary() {
		try {
			if (this.dictionary == null)
				init();
		} catch (Exception ex) {
			System.out.println("Error while retrieving the reference dictionary.\n" + ex.getMessage());
		}
		return this.dictionary;
	}

	public RefDictionary() {

	}

	/*
	 * This method will be called during service startup as the loading of
	 * dictionary is essential to the proper functioning of the service.
	 */
	@PostConstruct
	private void init() throws IOException {

		InputStream inputStream = null;
		BufferedReader buffReader = null;
		HttpsURLConnection conn = null;
		try {
			URL url = new URL(dictionaryURL + "/" + dictionaryName);
			logger.info("Loading dictionary using URL " + dictionaryURL + "/" + dictionaryName);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/text");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);

			// execute the request
			int status = conn.getResponseCode();
			logger.debug("Connection status is " + status);

			if (status == 200) {
				inputStream = conn.getInputStream();
				buffReader = new BufferedReader(new InputStreamReader(inputStream));
				String inputLine;
				this.dictionary = new HashSet<String>();
				while ((inputLine = buffReader.readLine()) != null) {
					this.dictionary.add(inputLine.trim().toLowerCase());
				}
				logger.info("Dictionary initialization complete. Loaded "
						+ (this.dictionary != null ? this.dictionary.size() : 0) + " words");
			} else {
				throw new IOException("Error while loading dictionary");
			}
			// Prepare connection to be closed
			conn.disconnect();

			// Invoking the close() method on InputStream will free URLConnection resource.
			buffReader.close();
			inputStream.close();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			// Free the resources
			if (conn != null)
				conn.disconnect();
			if (buffReader != null)
				buffReader.close();
			if (inputStream != null)
				inputStream.close();
		}
	}

}