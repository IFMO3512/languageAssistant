package com.fivehundredtwelve.langassist.controller.rest.response;

public class AddTranslationResponse {
	
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int TRANSLATION_IS_ALREADY_EXISTS = 2;
	
	private int status;
	private String description;
	
	public AddTranslationResponse() {}
	
	public AddTranslationResponse(int status) {
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
