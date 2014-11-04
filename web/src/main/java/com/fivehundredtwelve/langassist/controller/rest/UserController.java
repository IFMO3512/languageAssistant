package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.accounts.AccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            accountManager.putUser(new User(email));
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
                return addUser(email);      // TODO delete
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


    public Container setLanguage(final @CookieValue("name") String name, final @CookieValue("domain") String domain,
                                 final @RequestBody String language) {
        LOGGER.debug("Setting language={} for user with name={} and domain={}", language, name, domain);

        if (name == null || domain == null || language == null)
            return illegalArgumentsContainer("Name or domain or language is null");

        final Language userLanguage = Language.getLanguage(language);

        if (userLanguage == null)
            return illegalArgumentsContainer("Language not found");

        final String email = getEmail(name, domain);

        final User user = new User(email, userLanguage);
        LOGGER.debug("Created user={}", user);

        try {
            accountManager.putUser(user);
            return createSuccessContainer("User have been updated");
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
