package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountManagerImplTest {
    private final static User user = new User("user@gmail.com");

    private final static Word WORD = new Word("word", Languages.ENGLISH);

    private final static Word TRANSLATION = new Word("das Wort", Languages.GERMAN);

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
        accountManager.addTranslationToUser(user, WORD, TRANSLATION);

        assertEquals("User's dictionary should be empty because user does not exist", 0,
                accountManager.getTranslations(user, WORD).size());

        accountManager.addUser(user);
        accountManager.addTranslationToUser(user, WORD, TRANSLATION);

        assertTrue("Translation should be visible at user Translations",
                accountManager.getTranslations(user, WORD).contains(TRANSLATION));
    }
}