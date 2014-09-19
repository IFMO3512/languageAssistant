package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;

/**
 * Manages {User}s entities, stores them in database and checks them
 *
 * @author eliseev
 */
public interface AccountManager {
    public void addUser(User user);

    public boolean checkUser(User user);
}
