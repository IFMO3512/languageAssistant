package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;

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
    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public boolean checkUser(User user) {
        return users.get(user.getEmail()) != null;
    }
}
