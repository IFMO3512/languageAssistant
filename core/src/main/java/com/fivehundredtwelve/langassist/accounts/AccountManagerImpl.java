package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eliseev
 */
public class AccountManagerImpl implements AccountManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountManagerImpl.class);

    private final Map<String, User> users;

    private final Map<User, List<Word>> translations;

    public AccountManagerImpl() {
        users = new ConcurrentHashMap<>();
        translations = new ConcurrentHashMap<>();
    }

    @Override
    public void addUser(final @Nonnull User user) {
        Preconditions.checkNotNull(user);

        LOGGER.debug("Adding user with email={}", user.getEmail());

        users.put(user.getEmail(), user);

        LOGGER.debug("User added");
    }

    @Override
    public boolean checkUser(final @Nonnull User user) {
        Preconditions.checkNotNull(user);

        LOGGER.debug("Checking user with email={}", user.getEmail());

        return users.get(user.getEmail()) != null;
    }


    @Override
    public void addWordToUser(final @Nonnull User user, final @Nonnull Word word) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(word);

        LOGGER.debug("Adding word={} to user with email={}", user, word);

        // TODO in cookie is value without a @gmail
        if (checkUser(user)) {
            LOGGER.debug("User is correct, adding the word");

            List<Word> userWords = translations.getOrDefault(user, new ArrayList<>());

            userWords.add(word);
            translations.put(user, userWords);
        }

        LOGGER.debug("Addition of word to user is finished");
    }

    @Override
    @Nonnull
    public List<Word> getWords(final @Nonnull User user) {
        LOGGER.debug("Getting the words for user with email={}", user.getEmail());

        List<Word> userWords = translations.getOrDefault(user, new ArrayList<>());
        LOGGER.debug("Found {} words for {}", userWords.size(), user.getEmail());

        return userWords;
    }
}
