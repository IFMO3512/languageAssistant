package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.interfaces.AccountManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountManagerImplTest {
    private final static User user = new User("user@gmail.com");

    private final static Word WORD = new Word("word", Languages.ENGLISH);

    private AccountManager accountManager;

    @Before
    public void setUp() throws Exception {
        accountManager = new AccountManagerImpl();
    }

    @Test
    public void testAddUser() throws Exception {
        accountManager.addUser(user);

        assertTrue("User should exist in accountManager after addition", accountManager.checkUser(user));
    }

    @Test
    public void testUserTranslations() throws Exception {
        accountManager.addWordToUser(user, WORD);

        assertEquals("User's dictionary should be empty because user does not exist", 0,
                accountManager.getWords(user).size());

        accountManager.addUser(user);
        accountManager.addWordToUser(user, WORD);

        assertTrue("Translation should be visible at user Translations",
                accountManager.getWords(user).contains(WORD));
    }
}