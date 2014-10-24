package com.fivehundredtwelve.langassist.controller.rest;

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
 * Receives restful requests to manage users.
 *
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private DictionaryManager dictionaryManager;

    /**
     * Creates user account.
     *
     * @param email email of user
     * @return status of operation
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Container addUser(@RequestParam(value = "email", required = true) String email) {
        LOGGER.debug("Registering user with email={}", email);

        if (email == null)
            return new Container(ResponseCode.ILLEGAL_ARGUMENTS);

        try {
            LOGGER.debug("Adding user with email={}", email);
            accountManager.addUser(new User(email));
        } catch (RuntimeException e) {
            return createErrorContainer(e);
        }

        LOGGER.debug("User with email={} successfully registered", email);
        return new Container(ResponseCode.OK);
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Container checkUser(@RequestParam(value = "email", required = true) String email) {
        LOGGER.debug("Checking user with email={}", email);

        if (email == null)
            return new Container(ResponseCode.ILLEGAL_ARGUMENTS);

        try {
            LOGGER.debug("Checking user with email={}", email);
            if (accountManager.checkUser(new User(email))) {
                LOGGER.debug("User with email={} exists");
                return new Container(ResponseCode.OK);
            } else {
                LOGGER.warn("User with email={} does not exist");
                return new Container(ResponseCode.NOT_OK);
            }
        } catch (RuntimeException e) {
            return createErrorContainer(e);
        }
    }

    // TODO - implement, add appropriate parameters

    /**
     * Updates user account.
     */
    @RequestMapping("/update")
    public void updateUser() {

        throw new UnsupportedOperationException();

    }

    /**
     * Adds word to user.
     *
     * @param word the word itself
     * @return status of adding word to user
     */
    @RequestMapping("/dictionary/add")
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


    @RequestMapping(value = "/dictionary/remove", method = RequestMethod.POST)
    public Container deleteFromDictionary(@CookieValue("name") final String name,
                                          @CookieValue("domain") final String domain,
                                          @RequestBody final Word word) {
        LOGGER.debug("Removing word={} in language={} from user dictionary for user with name={} and domain={}",
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


    @RequestMapping("/dictionary/getall")
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

    private String getEmail(final String name, final String domain) {
        return String.format("%s@%s", name, domain);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
