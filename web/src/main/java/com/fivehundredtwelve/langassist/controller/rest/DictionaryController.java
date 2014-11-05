package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Receives restful requests to manage translations of words.
 *
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController extends AbstractController {
    private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryManager dictionaryManager;

    /**
     * Adds translation of word. Also adds word and word-translation in dictionary if they are not exist in it.
     *
     * @return status of adding translation
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Container addTranslation(@RequestBody WordWithTranslation word) {
        LOGGER.debug("Adding translation={}", word);

        if (word.getTranslation() == null)
            illegalArgumentsContainer("Translation should be setted");

        try {
            dictionaryManager.addTranslation(word, word.getTranslation());

            // TODO think about duplex translation
            dictionaryManager.addTranslation(word.getTranslation(), word);

            return createSuccessContainer("Translation in both way is added");

        } catch (RuntimeException e) {
            return createErrorContainer(e);
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
            Language foundLanguage = Language.getLanguage(translationLanguage);
            if (foundLanguage == null) {
                return new Container(ResponseCode.NOT_OK, "Language is not found");
            }

            translation = dictionaryManager.getTranslation(new Word(word, language), foundLanguage);

        } catch (IllegalArgumentException e) {
            return createErrorContainer(e);
        }

        if (translation == null)
            return new Container(ResponseCode.NOT_OK, "Translation is not found");

        LOGGER.debug("Returning not null translation={} from word={} in language={}",
                translation, word, language);

        return new DataContainer<>(ResponseCode.OK, translation);

    }

    @RequestMapping("/translations/get")
    public Container getTranslations(@RequestBody Word word) {
        LOGGER.debug("Getting translation for word={}", word);

        try {
            return createSuccessContainer("The translations are found: {}", dictionaryManager.getTranslations(word));
        } catch (Exception ex) {
            return createErrorContainer(ex);
        }
    }


    @RequestMapping("/getall")
    public Container getWords() {
        LOGGER.debug("Getting all words");

        try {
            return createSuccessContainer("System words are {}", dictionaryManager.getWords());
        } catch (Exception ex) {
            return createErrorContainer(ex);
        }
    }

    @RequestMapping("/translations/getall")
    public Container getWordsWithTranslation(@RequestParam final String language) {
        LOGGER.debug("Getting all words with translation to {} language", language);

        final Language _language = Language.getLanguage(language);

        if (_language == null) {
            illegalArgumentsContainer("Language was not found");
        }

        try {
            return createSuccessContainer("Found translations are:",
                    dictionaryManager.getWordsWithTranslation(_language));
        } catch (Exception ex) {
            return createErrorContainer(ex);
        }
    }

    @RequestMapping("/remove")
    public Container removeWord(@RequestBody Word word) {
        LOGGER.debug("Removing word={}", word);

        try {
            dictionaryManager.removeWord(word);

            return createSuccessContainer("Word have been removed");

        } catch (Exception ex) {
            return createErrorContainer(ex);
        }
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
