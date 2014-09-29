package com.fivehundredtwelve.langassist.controller.rest.response;

import com.fivehundredtwelve.langassist.Word;

public class GetTranslationResponse {
	
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int NO_TRANSLATION = 2;

	public static int getNoTranslation() {
		return NO_TRANSLATION;
	}
	private int status;
	private String description;
	private Word translation;
	
	public GetTranslationResponse() {}
	
	public GetTranslationResponse(int status) {
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
	public Word getTranslation() {
		return translation;
	}
	public void setTranslation(Word translation) {
		this.translation = translation;
	}
}
