package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author eliseev
 */
public class AccountManagerImpl implements AccountManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountManagerImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Override
    public void putUser(final @Nonnull User user) {
        Preconditions.checkNotNull(user);

        LOGGER.debug("Adding user with email={}", user.getEmail());

        accountDao.addUser(user);

        LOGGER.debug("User added");
    }

    @Override
    public boolean checkUser(final @Nonnull User user) {
        Preconditions.checkNotNull(user);

        LOGGER.debug("Checking user with email={}", user.getEmail());


        if (getUser(user.getEmail()) != null) {
            LOGGER.debug("User={} is found", user);
            return true;
        } else {
            LOGGER.debug("User={} is not found", user);
            return false;
        }
    }

    @Nullable
    @Override
    public User getUser(final @Nonnull String email) {
        Preconditions.checkNotNull(email);

        LOGGER.debug("Getting user with email={}", email);

        return accountDao.getUser(email);
    }

    @Override
    public void addWordToUser(final @Nonnull User user, final @Nonnull Word word) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(word);

        LOGGER.debug("Adding word={} to user with email={}", user, word);

        if (checkUser(user)) {
            accountDao.addWordToUser(user, word);
        }

        LOGGER.debug("Addition of word to user is finished");
    }

    @Override
    @Nonnull
    public Collection<Word> getWords(final @Nonnull User user) {
        LOGGER.debug("Getting the words for user with email={}", user.getEmail());

        List<Word> userWords = accountDao.getWords(user);

        LOGGER.debug("Found {} words for {}", userWords.size(), user.getEmail());

        return userWords;
    }

    @Override
    public void removerUserWord(final @Nonnull User user, final @Nonnull Word word) {
        Preconditions.checkNotNull(user, "User should be not null");
        Preconditions.checkNotNull(word, "Word should be not null");

        LOGGER.debug("Removing from user={} the word={}", user, word);

        if (checkUser(user)) {
            LOGGER.debug("User is correct, adding the word");

            accountDao.removeWord(user, word);

            LOGGER.debug("Word={} was removed", word);
        } else {
            LOGGER.debug("Can't find the user={}", user);
        }
    }
}
