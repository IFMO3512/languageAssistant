package com.fivehundredtwelve.langassist.controller.rest.response;

public class AddInUserDictionaryResponse {
	
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int WORD_IS_ALREADY_IN_DICTIONARY = 2;
	
	private int status;
	private String description;
	
	public AddInUserDictionaryResponse() {}
	
	public AddInUserDictionaryResponse(int status) {
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
	
}
