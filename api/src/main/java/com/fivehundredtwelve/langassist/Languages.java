package com.fivehundredtwelve.langassist;

/**
 * @author eliseev
 */
public enum Languages {
   
	ENGLISH,
    RUSSIAN,
    FRENCH,
    GERMAN,
    ITALIAN,
    SPANISH;
    
    
    public static Languages getLanguage(String language) {
    	
    	if (language.equals(Languages.ENGLISH.toString())) {
    		return Languages.ENGLISH;
    	} else if (language.equals(Languages.RUSSIAN.toString())) {
    		return Languages.RUSSIAN;
    	} else if (language.equals(Languages.FRENCH.toString())) {
    		return Languages.FRENCH;
    	} else if (language.equals(Languages.GERMAN.toString())) {
    		return Languages.GERMAN;
    	} else if (language.equals(Languages.ITALIAN.toString())) {
    		return Languages.ITALIAN;
    	} else if (language.equals(Languages.SPANISH.toString())) {
    		return Languages.SPANISH;
    	}
    	
    	throw new IllegalArgumentException("Illegal language name.");
    	
    }
	
}
