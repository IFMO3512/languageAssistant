package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.sun.istack.internal.NotNull;

/**
 * Manages {User}s entities, stores them in database and checks them
 *
 * @author eliseev
 */
public interface AccountManager {
    public void addUser(final @NotNull User user);

    public boolean checkUser(final @NotNull User user);
}
