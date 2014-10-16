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
    	
    	for(Languages lang: Languages.values()) {
    	    if(language.equalsIgnoreCase(lang.name())) {
    	      return lang;
    	    }
    	}
    	
    	throw new IllegalArgumentException("Illegal language name.");
    	
    }
	
}
