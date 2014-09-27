package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.interfaces.AccountManager;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eliseev
 */
public class AccountManagerImpl implements AccountManager {
    private final Map<String, User> users;

    private final Map<User, Map<Word, List<Word>>> translations;

    public AccountManagerImpl() {
        users = new ConcurrentHashMap<>();
        translations = new HashMap<>();
    }

    @Override
    public void addUser(final @Nonnull User user) {
        Preconditions.checkNotNull(user);

        users.put(user.getEmail(), user);
    }

    @Override
    public boolean checkUser(final @Nonnull User user) {
        Preconditions.checkNotNull(user);

        return users.get(user.getEmail()) != null;
    }


    @Override
    public void addTranslationToUser(final @Nonnull User user, final @Nonnull Word word,
                                     final @Nonnull Word translation) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(translation);

        if (checkUser(user)) {
            Map<Word, List<Word>> userDictionary = getUserDictionary(user);
            List<Word> wordTranslations = userDictionary.getOrDefault(word, new ArrayList<>());

            wordTranslations.add(translation);
            userDictionary.put(word, wordTranslations);

            translations.put(user, userDictionary);
        }
    }

    @Override
    @Nonnull
    public List<Word> getTranslations(final @Nonnull User user, final @Nonnull Word word) {
        Map<Word, List<Word>> userDictionary = getUserDictionary(user);

        return userDictionary.getOrDefault(word, new ArrayList<>());
    }

    private Map<Word, List<Word>> getUserDictionary(User user) {
        return translations.getOrDefault(user, new HashMap<>());
    }
}
