package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.accounts.AccountManager;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author eliseev
 */
@RestController
@RequestMapping("/user/dictionary")
public class UserDictionaryController extends AbstractController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserDictionaryController.class);

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private DictionaryManager dictionaryManager;

    /**
     * Adds word to user.
     *
     * @param word the word itself
     * @return status of adding word to user
     */
    @RequestMapping("/add")
    public Container addInUserDictionary(@RequestBody Word word, @CookieValue("name") String name,
                                         @CookieValue("domain") String domain) {
        LOGGER.debug("Adding word={} to the dictionary of user with name={} and domain={}", word, name, domain);

        if (word == null || name == null || domain == null) {
            return new Container(ResponseCode.ILLEGAL_ARGUMENTS);
        }

        String email = getEmail(name, domain);

        try {
            accountManager.addWordToUser(new User(email), word);

            dictionaryManager.addWord(word);

            LOGGER.debug("Word={} added to user with email={}", word, email);

            return new Container(ResponseCode.OK);

        } catch (Exception ex) {
            return createErrorContainer(ex);
        }
    }


    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public Container deleteFromDictionary(@CookieValue("name") final String name,
                                          @CookieValue("domain") final String domain, @RequestBody final Word word) {
        LOGGER.debug("Removing word={} from user dictionary for user with name={} and domain={}",
                word, name, domain);

        if (name == null || domain == null || word == null)
            return new Container(ResponseCode.ILLEGAL_ARGUMENTS);

        final String email = getEmail(name, domain);

        final User user = new User(email);
        LOGGER.debug("Created user={}", user);

        try {
            accountManager.removerUserWord(user, word);
            return createSuccessContainer("Word should be removed");
        } catch (Exception ex) {
            return createErrorContainer(ex);
        }

    }


    @RequestMapping("/getall")
    public Container getUserDictionary(@CookieValue("name") final String name,
                                       @CookieValue("domain") final String domain) {
        LOGGER.debug("Getting user dictionary for user with name={} and domain={}", name, domain);

        if (name == null || domain == null)
            return new Container(ResponseCode.ILLEGAL_ARGUMENTS);

        final String email = getEmail(name, domain);

        User user = new User(email);

        try {
            Collection<Word> words = accountManager.getWords(user);

            LOGGER.debug("User with email={} have a dictionary={}", email, words);

            return new DataContainer<>(ResponseCode.OK, words);

        } catch (Exception ex) {
            return createErrorContainer(ex);
        }
    }


    @RequestMapping("/getall/translation")
    public Container getUserDictionaryWithTranslation(@CookieValue("name") final String name,
                                                      @CookieValue("domain") final String domain,
                                                      @RequestParam final String language) {
        LOGGER.debug("Getting user dictionary for user with name={} and domain={}", name, domain);

        if (name == null || domain == null)
            return new Container(ResponseCode.ILLEGAL_ARGUMENTS);

        Language _language = Language.getLanguage(language);

        if (_language == null) {
            _language = Language.ENGLISH;   // TODO delete, Standard language
        }

        final String email = getEmail(name, domain);

        User user = new User(email);

        try {
            Collection<Word> words = accountManager.getWords(user);

            LOGGER.debug("User with email={} have a dictionary={}", email, words);

            Collection<Word> wordsWithTranslation = dictionaryManager.getWordsWithTranslation(words,
                    _language);

            return new DataContainer<>(ResponseCode.OK, wordsWithTranslation);

        } catch (Exception ex) {
            return createErrorContainer(ex);
        }
    }

    private String getEmail(final String name, final String domain) {
        return String.format("%s@%s", name, domain);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
