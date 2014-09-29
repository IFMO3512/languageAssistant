package com.fivehundredtwelve.langassist.controller.rest.response;

import com.fivehundredtwelve.langassist.Word;

final public class GetTranslationResponse {
	
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int NO_TRANSLATION = 2;

	private final int status;
	private final String description;
	private final Word translation;
	
	public GetTranslationResponse(int status, Word translation) {
		this.status = status;
		this.description = "";
		this.translation = translation;
	}
	
	public GetTranslationResponse(int status, String description, Word translation) {
		this.status = status;
		this.description = description;
		this.translation = translation;
	}
	
	public GetTranslationResponse(int status, String description) {
		this.status = status;
		this.description = description;
		this.translation = null;
	}
	
	public GetTranslationResponse(int status) {
		this.status = status;
		this.description = "";
		this.translation = null;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Word getTranslation() {
		return translation;
	}
}
