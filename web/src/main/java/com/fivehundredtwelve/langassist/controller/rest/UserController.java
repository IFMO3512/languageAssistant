package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.accounts.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Receives restful requests to manage users.
 *
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/user")
public class UserController {

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

        try {
            accountManager.addUser(new User(email));
        } catch (RuntimeException e) {
            return new Container(ResponseCode.ERROR);
        }

        return new Container(ResponseCode.OK);
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Container checkUser(@RequestParam(value = "email", required = true) String email) {

        try {
            accountManager.addUser(new User(email));
        } catch (RuntimeException e) {
            return new Container(ResponseCode.ERROR);
        }

        return new Container(ResponseCode.OK);
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
     * @param word     the word itself
     * @return status of adding word to user
     */
    @RequestMapping("/dictionary/add")
    public Container addInUserDictionary(@RequestBody Word word, @CookieValue("user") String user) {
        if(word == null || user == null) {
            return new Container(ResponseCode.ILLEGAL_ARGUMENTS);
        }

        try {
            accountManager.addWordToUser(new User(user), word);
            return new Container(ResponseCode.OK);
        } catch (Exception ex) {
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
     * @param languige language of expected words
     * @return
     */
    @RequestMapping("/dictionary/get")
    public Container getFromUserDictionary(@RequestParam(value = "language", required = true) String language) {

        // TODO - request appropriate accountManager method, return body
        throw new UnsupportedOperationException();

    }
}
