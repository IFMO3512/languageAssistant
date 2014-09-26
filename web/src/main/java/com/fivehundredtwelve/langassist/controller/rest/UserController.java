package com.fivehundredtwelve.langassist.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.controller.rest.response.AddInUserDictionaryResponse;
import com.fivehundredtwelve.langassist.controller.rest.response.AddUserResponse;
import com.fivehundredtwelve.langassist.controller.rest.response.GetFromUserDictionaryResponse;
import com.fivehundredtwelve.langassist.interfaces.AccountManager;

/**
 * Receives restful requests to manage users.
 * 
 * @author igor-ryabchikov
 */
@RestController
@RequestMapping("/user")
public class UserController {
	
	private AccountManager accountManager;
	
	/**
	 * Creates user account.
	 * 
	 * @param email email of user
	 * @return status of operation
	 */
	@RequestMapping("/add")
	public AddUserResponse addUser(@RequestParam(value = "email", required = true) String email) {
		
		try {
			accountManager.addUser(new User(email));
		} catch(RuntimeException e) {
			return new AddUserResponse(AddUserResponse.ERROR);
		}
		
		return new AddUserResponse(AddUserResponse.SUCCESS);
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
	 * @param lanquage language of the word
	 * @return status of adding word to user
	 */
	@RequestMapping("/dictionary/add")
	public AddInUserDictionaryResponse addInUserDictionary(@RequestParam(value = "word", required = true) String word, @RequestParam(value = "word", required = true) int lanquage) {
		
		// TODO - request appropriate accountManager method, return body
		throw new UnsupportedOperationException();
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
	public GetFromUserDictionaryResponse getFromUserDictionary(@RequestParam(value = "language", required = true) int languige) {
		
		// TODO - request appropriate accountManager method, return body
		throw new UnsupportedOperationException();
		
	}
}
