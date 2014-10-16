package com.fivehundredtwelve.langassist.controller.rest.response;

import com.fivehundredtwelve.langassist.Word;

final public class GetFromUserDictionaryResponse {
	
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	
	private final int status;
	private final String description;
	private final Word[] words;
	
	public GetFromUserDictionaryResponse(final int status, final String description) {
		this.status = status;
		this.description = description;
		this.words = null;
	}
	
	public GetFromUserDictionaryResponse(int status, String description, final Word[] words) {
		this.status = status;
		this.description = description;
		this.words = words;
	}
	
	public GetFromUserDictionaryResponse(int status, final Word[] words) {
		this.status = status;
		this.description = "";
		this.words = words;
	}
	
	public GetFromUserDictionaryResponse(int status) {
		this.status = status;
		this.description = "";
		this.words = null;
	}

	public int getStatus() {
		return status;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Word[] getWords() {
		return words;
	}
	
}
