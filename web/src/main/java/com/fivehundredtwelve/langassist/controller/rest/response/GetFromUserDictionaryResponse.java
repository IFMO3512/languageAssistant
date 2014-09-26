package com.fivehundredtwelve.langassist.controller.rest.response;

import com.fivehundredtwelve.langassist.Word;

public class GetFromUserDictionaryResponse {
	
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	
	private int status;
	private String description;
	private Word[] words;
	
	public GetFromUserDictionaryResponse() {}
	
	public GetFromUserDictionaryResponse(int status) {
		setStatus(status);
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Word[] getWords() {
		return words;
	}
	public void setWords(Word[] words) {
		this.words = words;
	}
	
}
