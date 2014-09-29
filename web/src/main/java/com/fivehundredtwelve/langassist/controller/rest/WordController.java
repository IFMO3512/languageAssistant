package com.fivehundredtwelve.langassist.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.controller.rest.response.AddWordResponse;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;

/**
 * Receives restful requests to manage words.
 * 
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/rest/word")
public class WordController {
	
	@Autowired
	private DictionaryManager translatorManager;
	
	/**
	 * Adds word in system dictionary.
	 * 
	 * @param word the word itself
	 * @param language language of the word
	 * @return
	 */
	@RequestMapping("/add")
	public AddWordResponse addWord(@RequestParam(value = "word", required = true) String word,
			@RequestParam(value = "language", required = true) String language
			) {
		
		try {
			translatorManager.addWord(new Word(word, Languages.getLanguage(language)));
		}catch(IllegalArgumentException e) {
			// Wrong language name
			return new AddWordResponse(AddWordResponse.ERROR, e.getMessage());
		}catch(RuntimeException e) {
			return new AddWordResponse(AddWordResponse.ERROR);
		}
		
		return new AddWordResponse(AddWordResponse.SUCCESS);
	}
	
}