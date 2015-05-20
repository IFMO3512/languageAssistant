package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class AccountManagerImplTest {
    private final static User user = new User("user@gmail.com");

    private final static Word WORD = new Word("word", Language.ENGLISH);

    private AccountManager accountManager;

    @Before
    public void setUp() throws Exception {
        accountManager = new AccountManagerImpl();
    }

    @Test
    public void testAddUser() throws Exception {
        accountManager.putUser(user);

        assertTrue("User should exist in accountManager after addition", accountManager.checkUser(user));
    }

    @Test
    public void testUserTranslations() throws Exception {
        accountManager.addWordToUser(user, WORD);

        assertEquals("User's dictionary should be empty because user does not exist", 0,
                accountManager.getWords(user).size());

        accountManager.putUser(user);
        accountManager.addWordToUser(user, WORD);

        assertTrue("Translation should be visible at user Translations",
                accountManager.getWords(user).contains(WORD));

        accountManager.removerUserWord(user, WORD);

        assertFalse("Translation shouldn't be visible when it was removed",
                accountManager.getWords(user).contains(WORD));
    }
}