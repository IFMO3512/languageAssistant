package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryManager dictionaryManager;

    /**
     * Adds translation of word. Also adds word and word-translation in dictionary if they are not exist in it.
     *
     * @return status of adding translation
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Container addTranslation(@RequestBody Translation translation) {
        LOGGER.debug("Adding translation={}", translation);

        try {
            dictionaryManager.addTranslation(translation.getSource(), translation.getTranslation());

            // TODO think about this decision
            dictionaryManager.addTranslation(translation.getTranslation(), translation.getSource());

            LOGGER.debug("Translation in both way is added");

            return new Container(ResponseCode.OK);

        } catch (RuntimeException e) {
            LOGGER.debug("Exception occurred", e);
            return new Container(ResponseCode.ERROR, e.getMessage());
        }
    }

    /**
     * Returns translation of the word.
     *
     * @param word                the word itself
     * @param language            language of the word
     * @param translationLanguage language of expected translation
     * @return status of operation and word-translation, if status is 0
     */
    @RequestMapping("/get")
    public Container getTranslation(@RequestParam(value = "word", required = true) String word,
                                    @RequestParam(value = "wordLanguage", required = true) String language,
                                    @RequestParam(value = "translationLanguage", required = true) String
                                            translationLanguage) {
        LOGGER.debug("Getting translation for word={} in language={} to language={}",
                word, language, translationLanguage);

        final Word translation;
        try {
            translation = dictionaryManager.getTranslation(new Word(word, language),
                    Language.getLanguage(translationLanguage)); // TODO error is better then npe

        } catch (IllegalArgumentException e) {
            LOGGER.debug("Exception occurred", e);
            return new Container(ResponseCode.ERROR, e.getMessage());
        }

        if (translation == null)
            return new Container(ResponseCode.NOT_OK, "error");

        LOGGER.debug("Returning not null translation={} from word={} in language={}",
                translation, word, language);

        return new DataContainer<>(ResponseCode.OK, translation);

    }

    @RequestMapping("/translations/get")
    public Container getTranslations(@RequestBody Word word) {
        LOGGER.debug("Getting translation for word={}", word);

        try {
            Collection<Word> words = dictionaryManager.getTranslations(word);
            LOGGER.debug("Word={} is translated as {}", word, words);

            return new DataContainer<>(ResponseCode.OK, words);
        } catch (Exception ex) {
            LOGGER.debug("Exception occurred", ex);
            return new Container(ResponseCode.ERROR, ex.getMessage());
        }
    }


    @RequestMapping("/getall")
    public Container getWords() {
        LOGGER.debug("Getting all words");

        try {
            Collection<Word> words = dictionaryManager.getWords();
            LOGGER.debug("System words are {}", words);

            return new DataContainer<>(ResponseCode.OK, words);

        } catch (Exception ex) {
            LOGGER.debug("Exception occurred", ex);
            return new Container(ResponseCode.ERROR, ex.getMessage());
        }
    }

    @RequestMapping("/remove")
    public Container removeWord(@RequestBody Word word) {
        LOGGER.debug("Removing word={}", word);

        try {
            dictionaryManager.removeWord(word);

            LOGGER.debug("Word have been removed");
            return new Container(ResponseCode.OK);

        } catch (Exception ex) {
            LOGGER.debug("Exception occurred", ex);
            return new Container(ResponseCode.ERROR, ex.getMessage());
        }
    }

}
