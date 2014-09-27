package com.fivehundredtwelve.langassist.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.controller.rest.response.AddTranslationResponse;
import com.fivehundredtwelve.langassist.controller.rest.response.GetTranslationResponse;
import com.fivehundredtwelve.langassist.interfaces.DictionaryManager;
import com.google.common.base.Preconditions;

/**
 * Receives restful requests to manage translations of words.
 * 
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/translator")
public class TranslatorController {
	
	@Autowired
	public DictionaryManager translatorManager;
	
	/**
	 * Adds translation of word. Also adds word and word-translation in dictionary if they are not exist in it.
	 * 
	 * @param word the word itself
	 * @param language language of the word
	 * @param translationWord word-translation
	 * @param translationLanguage language of translation
	 * @return status of adding translation
	 */
	@RequestMapping("/add")
	public AddTranslationResponse addTranslation(@RequestParam(value = "word", required = true) String word,
			@RequestParam(value = "wordLanquage", required = true) String language,
			@RequestParam(value = "translationWord", required = true) String translationWord,
			@RequestParam(value = "translationLanquage", required = true) String translationLanguage
			) {
		
		try {
			translatorManager.addTranslation(new Word(word, new Languages(language)), new Word(translationWord, new Languages(translationLanguage)));
		}catch(IllegalArgumentException e) {
			// Wrong language name
			return new AddTranslationResponse(AddTranslationResponse.ERROR);
		}catch(RuntimeException e) {
			return new AddTranslationResponse(AddTranslationResponse.ERROR);
		}
		
		return new AddTranslationResponse(AddTranslationResponse.SUCCESS);
	}
	
	/**
	 * Returns translation of the word.
	 * 
	 * @param word the word itself
	 * @param language language of the word
	 * @param translationLanguage language of expected translation
	 * @return status of operation and word-translation, if status is 0
	 */
	@RequestMapping("/get")
	public GetTranslationResponse getTranslations(@RequestParam(value = "word", required = true) String word,
			@RequestParam(value = "wordLanquage", required = true) String language,
			@RequestParam(value = "translationLanquage", required = true) String translationLanguage) {
		
		Word translation = null;
		try {
			translation = translatorManager.getTranslation(new Word(word, new Languages(language)), new Languages(translationLanguage));
		}catch(IllegalArgumentException e) {
			// Wrong language name
			return new GetTranslationResponse(GetTranslationResponse.ERROR);
		}catch(RuntimeException e) {
			return new GetTranslationResponse(GetTranslationResponse.ERROR);
		}
		
		return new GetTranslationResponse(GetTranslationResponse.NO_TRANSLATION);
		
		GetTranslationResponse response = new GetTranslationResponse(GetTranslationResponse.SUCCESS);
		response.setTranslation(translation);
		
		return response;
	}
	
}
