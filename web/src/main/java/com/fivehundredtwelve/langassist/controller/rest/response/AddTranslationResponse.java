package com.fivehundredtwelve.langassist.controller.rest.response;

final public class AddTranslationResponse {
	
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int TRANSLATION_IS_ALREADY_EXISTS = 2;
	
	private final int status;
	private final String description;
	
	public AddTranslationResponse(final int status, final String description) {
		this.status = status;
		this.description = description;
	}
	
	public AddTranslationResponse(final int status) {
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
