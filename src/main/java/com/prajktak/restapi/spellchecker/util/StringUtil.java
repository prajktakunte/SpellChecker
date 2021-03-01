package com.prajktak.restapi.spellchecker.util;

import org.springframework.stereotype.Component;

/*
 * This module contains string utility functions. 
 * @author: Prajkta Kunte
 */
@Component
public class StringUtil {

	//Removes characters that are contiguously repeated more than 2 times.
	public String removeRepeats(String inStr) 
    { 
		 
	        String str = new String(); 
	        int len = inStr.length(); 
	        char[] arr =   inStr.toCharArray();
	        str += Character.toString(arr[0]); 
	        for (int i = 1; i < len; i++)  
	        {   	        	
	            if ((arr[i-1] != arr[i] || arr[i-2] != arr[i]))
	            { 	                 
	                str += Character.toString(arr[i]); 
	            } 
	        }
	        return str;
    } 
	
	//Checks if alphabet is a vowel.
	public boolean isVowel(char ch) {
		return (ch == 'a' || ch == 'e'|| ch == 'i'|| ch == 'o'|| ch == 'u');
	}	
}
