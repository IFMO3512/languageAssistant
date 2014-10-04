package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Container addTranslation(@RequestParam(value = "word", required = true) String word,
                                    @RequestParam(value = "wordLanguage", required = true) String language,
                                    @RequestParam(value = "translationWord", required = true) String translationWord,
			@RequestParam(value = "translationLanguage", required = true) String translationLanguage
			) {
		
		try {
			translatorManager.addTranslation(new Word(word, Languages.getLanguage(language)), new Word(translationWord, Languages.getLanguage(translationLanguage)));
		}catch(IllegalArgumentException e) {
			// Wrong language name
            return new Container(ResponseCode.ERROR, e.getMessage());
        } catch (RuntimeException e) {
            return new Container(ResponseCode.ERROR);
        }

        return new Container(ResponseCode.OK);
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
    public Container getTranslation(@RequestParam(value = "word", required = true) String word,
                                    @RequestParam(value = "wordLanguage", required = true) String language,
                                    @RequestParam(value = "translationLanguage", required = true) String translationLanguage) {

        final Word translation;
        try {
            translation = translatorManager.getTranslation(new Word(word, Languages.getLanguage(language)), Languages.getLanguage(translationLanguage));
		}catch(IllegalArgumentException e) {
			// Wrong language name
            return new Container(ResponseCode.ERROR, e.getMessage());
        } catch (RuntimeException e) {

            return new Container(ResponseCode.ERROR, e.toString());
        }

        if (translation == null)
            return new Container(ResponseCode.NO_TRANSLATION, "error");

        return new DataContainer<>(ResponseCode.OK, translation);

    }

}
