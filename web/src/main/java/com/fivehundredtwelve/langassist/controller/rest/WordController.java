package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

/**
 * Receives restful requests to manage words.
 *
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/rest/word")
public class WordController extends AbstractController {
    private final Logger LOGGER = LoggerFactory.getLogger(WordController.class);

    @Autowired
    private DictionaryManager translatorManager;

    /**
     * Adds word in system dictionary.
     *
     * @param word     the word itself
     * @param language language of the word
     */
    @RequestMapping("/add")
    public Container addWord(@RequestParam(value = "word", required = true) String word,
                             @RequestParam(value = "language", required = true) String language) {
        LOGGER.debug("Adding {} word={} to the system", language, word);

        try {
            translatorManager.addWord(new Word(word, language));

            return createSuccessContainer("Word has been added");
        } catch (RuntimeException e) {
            return createErrorContainer(e);
        }
    }

    @Override
    @Nonnull
    protected Logger getLogger() {
        return LOGGER;
    }
}
