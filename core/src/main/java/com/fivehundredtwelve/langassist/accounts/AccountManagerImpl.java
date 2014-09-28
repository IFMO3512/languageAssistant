package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
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

    private final Map<User, List<Word>> translations;

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
    public void addWordToUser(final @Nonnull User user, final @Nonnull Word word) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(word);

        if (checkUser(user)) {
            List<Word> userWords = translations.getOrDefault(user, new ArrayList<>());

            userWords.add(word);
            translations.put(user, userWords);
        }
    }

    @Override
    @Nonnull
    public List<Word> getWords(final @Nonnull User user) {
        return translations.getOrDefault(user, new ArrayList<>());
    }
}
