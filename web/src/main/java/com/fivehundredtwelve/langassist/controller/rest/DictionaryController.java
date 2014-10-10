package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Receives restful requests to manage translations of words.
 * 
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryManager dictionaryManager;

    /**
	 * Adds translation of word. Also adds word and word-translation in dictionary if they are not exist in it.
     *
     * @return status of adding translation
	 */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Container addTranslation(@RequestBody Translation translation) {
        try {
            dictionaryManager.addTranslation(translation.getSource(), translation.getTranslation());
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
                                    @RequestParam(value = "translationLanguage", required = true) String
                                            translationLanguage) {
        final Word translation;
        try {
            translation = dictionaryManager.getTranslation(new Word(word, language),
                    Language.getLanguage(translationLanguage));
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

    @RequestMapping("/translations/get")
    public Container getTranslations(@RequestBody Word word) {
        try {
            return new DataContainer<>(ResponseCode.OK, dictionaryManager.getTranslations(word));
        } catch (Exception ex) {
            return new Container(ResponseCode.ERROR, ex.getMessage());
        }
    }


    @RequestMapping("/getall")
    public Container getWords() {
        final Collection<Word> words;
        try {
            words = dictionaryManager.getWords();
        } catch (Exception ex) {
            return new Container(ResponseCode.ERROR, ex.getMessage());
        }

        return new DataContainer<>(ResponseCode.OK, words);
    }

    @RequestMapping("/remove")
    public Container removeWord(@RequestBody Word word) {
        try {
            dictionaryManager.removeWord(word);
        } catch (Exception ex) {
            return new Container(ResponseCode.ERROR, ex.getMessage());
        }
        return new Container(ResponseCode.OK);
    }

}
