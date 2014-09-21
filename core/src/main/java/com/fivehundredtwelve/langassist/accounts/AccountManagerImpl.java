package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eliseev
 */
public class AccountManagerImpl implements AccountManager {
    private final Map<String, User> users;

    public AccountManagerImpl() {
        users = new ConcurrentHashMap<>();
    }

    @Override
    public void addUser(final @NotNull User user) {
        Preconditions.checkNotNull(user != null);

        users.put(user.getEmail(), user);
    }

    @Override
    public boolean checkUser(final @NotNull User user) {
        return users.get(user.getEmail()) != null;
    }
}
