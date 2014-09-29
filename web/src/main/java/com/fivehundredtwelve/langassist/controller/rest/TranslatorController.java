package com.fivehundredtwelve.langassist.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.controller.rest.response.AddTranslationResponse;
import com.fivehundredtwelve.langassist.controller.rest.response.GetTranslationResponse;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;

/**
 * Receives restful requests to manage translations of words.
 * 
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/rest/translator")
public class TranslatorController {
	
	@Autowired
	private DictionaryManager translatorManager;
	
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
			@RequestParam(value = "wordLanguage", required = true) String language,
			@RequestParam(value = "translationWord", required = true) String translationWord,
			@RequestParam(value = "translationLanguage", required = true) String translationLanguage
			) {
		
		try {
			translatorManager.addTranslation(new Word(word, Languages.getLanguage(language)), new Word(translationWord, Languages.getLanguage(translationLanguage)));
		}catch(IllegalArgumentException e) {
			// Wrong language name
			return new AddTranslationResponse(AddTranslationResponse.ERROR, e.getMessage());
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
	public GetTranslationResponse getTranslation(@RequestParam(value = "word", required = true) String word,
			@RequestParam(value = "wordLanguage", required = true) String language,
			@RequestParam(value = "translationLanguage", required = true) String translationLanguage) {
		
		Word translation = null;
		try {
			translation = translatorManager.getTranslation(new Word(word, Languages.getLanguage(language)), Languages.getLanguage(translationLanguage));
		}catch(IllegalArgumentException e) {
			// Wrong language name
			return new GetTranslationResponse(GetTranslationResponse.ERROR, e.getMessage());
		}catch(RuntimeException e) {
			
			return new GetTranslationResponse(GetTranslationResponse.ERROR, e.toString());
		}
		
		if (translation == null) new GetTranslationResponse(GetTranslationResponse.NO_TRANSLATION, "error");
		
		return new GetTranslationResponse(GetTranslationResponse.SUCCESS, translation);
		
	}
	
}
