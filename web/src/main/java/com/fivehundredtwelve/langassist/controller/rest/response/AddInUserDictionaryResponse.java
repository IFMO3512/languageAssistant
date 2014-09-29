package com.fivehundredtwelve.langassist.controller.rest.response;

final public class AddInUserDictionaryResponse {
	
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int WORD_IS_ALREADY_IN_DICTIONARY = 2;
	
	private final int status;
	private final String description;
	
	public AddInUserDictionaryResponse(final int status, final String description) {
		this.status = status;
		this.description = description;
	}
	
	public AddInUserDictionaryResponse(final int status) {
		this.status = status;
		this.description = "";
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getDescription() {
		return description;
	}
	
}
