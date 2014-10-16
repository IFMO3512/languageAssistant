package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.accounts.AccountManager;
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
public class UserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AccountManager accountManager;

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
            LOGGER.warn("An error occurred", e);
            return new Container(ResponseCode.ERROR);
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
            return new Container(ResponseCode.ERROR);
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

            LOGGER.debug("Word={} added to user with email={}", word, email);

            return new Container(ResponseCode.OK);

        } catch (Exception ex) {
            LOGGER.warn("Exception occurred during addition the word", ex);
            return new Container(ResponseCode.ERROR, ex.getMessage());
        }
    }

	/*
    // TODO - implement, maybe
	@RequestMapping("/dictionary/delete")
	public void deleteFromDictionary() {
		
		throw new UnsupportedOperationException();
		
	}
	*/

    /**
     * Returns all users words of specified language.
     *
     * @param language language of expected words
     */
    @RequestMapping("/dictionary/get")
    public Container getFromUserDictionary(@RequestParam(value = "language", required = true) String language) {

        // TODO - request appropriate accountManager method, return body
        throw new UnsupportedOperationException();

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
            LOGGER.debug("Exception occurred in getUserDictionary method", ex);

            return new Container(ResponseCode.ERROR, ex.getMessage());
        }
    }

    private String getEmail(final String name, final String domain) {
        return String.format("%s@%s", name, domain);
    }
}
