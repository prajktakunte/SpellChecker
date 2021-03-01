package com.prajktak.restapi.spellchecker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prajktak.restapi.spellchecker.data.RefDictionary;

/*
 * This module contains methods to check spelling and to suggest alternatives in case of wrong spelling.
 * This module uses Wagner–Fischer algorithm algorithm to compute the Levenshtein distance(edit distance between words).
 * @author: Prajkta Kunte
 */
@Component
public class SpellMatcher {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SpellMatcher.class);

	@Autowired
	private RefDictionary refdictionary;

	@Autowired
	private StringUtil strUtil;

	private final int minPercentMatch = 70;

	/*
	 * Checks if input word matches with one of the words in the reference dictionary.
	 */
	public boolean isCorrectSpelling(String inputWord) {
		if (inputWord == null)
			return false;
		
		String inputWordLower = inputWord.toLowerCase();
		return this.refdictionary.getDictionary().contains(inputWordLower);
	}

	/*
	 * Returns a list of words which can be constructed by inserting,deleting or
	 * changing some characters in the input string.
	 */
	public List<String> findSuggestions(String inStr) {

		if (inStr == null)
			return null;
		logger.info("Find suggestions for " + inStr);

		// Preprocess input string
		String preProcessedStr = preProcess(inStr);
		logger.info("Input string after preprocessing " + preProcessedStr);

		int wordLen = preProcessedStr.length();
		int currMinEditDistance = wordLen;
		
		Iterator<String> dictionaryIter = this.refdictionary.getDictionary().iterator();
		
		//Collection to store suggestions
		Map<Integer, List<String>> wordMap = new HashMap<Integer, List<String>>();

		// Compare the word with every word in the dictionary and find the closest match
		while (dictionaryIter.hasNext()) {
			String dictWord = dictionaryIter.next().trim();
			if (!dictWord.isBlank()) {

				int editDistance = getEditDistance(dictWord, preProcessedStr);				
				float percentMatch = ((float) Math.abs((wordLen - editDistance)) / wordLen) * 100;
				
				// Discard the suggestions that have more than desired number of edits.
				if (percentMatch >= minPercentMatch) {
					
					if (!wordMap.containsKey(editDistance)) {						
						wordMap.put(editDistance, new ArrayList<String>());
					}						
					wordMap.get(editDistance).add(dictWord);					

					// Track the lowest edit distance so far.
					currMinEditDistance = Math.min(editDistance, currMinEditDistance);
				}
			}
		}

		logger.debug("Minimum edit distance is " + currMinEditDistance);
		// Return the record with lowest edit distance since these words will be closest
		// match to the input string.
		return wordMap.get(currMinEditDistance);
	}
	
	/* Preprocess input string to :
	 *  - remove contiguous more than 2 repeating characters.
	 *  - convert to lower case.
	 */
	private String preProcess(String inStr) {
		if (inStr == null)
			return null;
        inStr = inStr.toLowerCase();
		return strUtil.removeRepeats(inStr);
	}	

	private int getEditDistance(String dictionaryWord, String word) {
		if (dictionaryWord == null && word == null)
			return 0;

		if (word.equalsIgnoreCase(dictionaryWord))
			return 0;

		int editDistance = levenshteinDistance(word, dictionaryWord);
		return editDistance;
	}

	/*
	 * Function to calculate Levenshtein distance between two words.
	 */
	private int levenshteinDistance(String sourceStr, String targetStr) {

		logger.debug("Calculating Levenshtein distance");
		int levenshteinDistance = Integer.MAX_VALUE;
		if (targetStr == null && sourceStr == null) {
			return levenshteinDistance;
		}

		int sourceLen = sourceStr.length();
		int targetLen = targetStr.length();
		char[] sourceArr = sourceStr.toCharArray();
		char[] targetArr = targetStr.toCharArray();
		int[][] matrix = new int[sourceLen + 1][targetLen + 1];
		// source prefixes can be transformed into empty string by 
		// dropping all characters
		for (int i = 1; i <= sourceLen; i++) {
			matrix[i][0] = i;
		}

		// target prefixes can be reached from empty source prefix
		// by inserting every character
		for (int j = 1; j <= targetLen; j++) {
			matrix[0][j] = j;
		}

		int substitutionCost = 0;
		int deletionCost = 0;
		int insertionCost = 0;
		for (int j = 1; j <= targetLen; j++) {

			for (int i = 1; i <= sourceLen; i++) {

				deletionCost = 1;
				
				//String input string could have 1 or more missing vowels,setting insertion cost for vowels as 0.
				if (strUtil.isVowel(targetArr[j - 1]))
					insertionCost = 0;
				else
					insertionCost = 1;

				if (sourceArr[i - 1] == targetArr[j - 1])
					substitutionCost = 0;
				//Setting substitution cost for changing source consonant to target consonant since we are changing the sound
				else if (!strUtil.isVowel(targetArr[j - 1]) && !strUtil.isVowel(sourceArr[i - 1]))
					substitutionCost = 2;
				else
					substitutionCost = 1;

				matrix[i][j] = Math.min(Math.min(matrix[i - 1][j] + deletionCost, // deletion
						                         matrix[i][j - 1] + insertionCost), // insertion
						                matrix[i - 1][j - 1] + substitutionCost); // substitution
			}
		}

		levenshteinDistance = matrix[sourceLen][targetLen];

		logger.debug("Edit distance between " + sourceStr + " and " + targetStr + " : " + levenshteinDistance);
		return levenshteinDistance;

	}

}
