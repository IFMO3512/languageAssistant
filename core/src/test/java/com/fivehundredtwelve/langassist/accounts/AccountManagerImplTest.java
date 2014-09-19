package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AccountManagerImplTest {
    private final static User user = new User("user@gmail.com");

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
}