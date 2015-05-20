package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eliseev
 */
public class AccountDaoCollections implements AccountDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountDaoCollections.class);

    private final Map<String, User> users;

    private final Map<User, List<Word>> userDictionary;

    public AccountDaoCollections() {
        users = new ConcurrentHashMap<>();
        userDictionary = new ConcurrentHashMap<>();
    }

    @Override
    public void addUser(final User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public User getUser(final String email) {
        return users.get(email);
    }

    @Override
    public void addWordToUser(final User user, final Word word) {
        LOGGER.debug("User is correct, adding the word");

        List<Word> userWords = userDictionary.getOrDefault(user, new ArrayList<>());

        userWords.add(word);
        userDictionary.put(user, userWords);
    }

    @Override
    public List<Word> getWords(final User user) {
        return userDictionary.getOrDefault(user, new ArrayList<>());
    }

    @Override
    public void removeWord(final User user, final Word word) {
        List<Word> userWords = userDictionary.getOrDefault(user, new ArrayList<>());

        userWords.remove(word);
        userDictionary.put(user, userWords);
    }
}
