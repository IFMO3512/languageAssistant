package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Manages {User}s entities, stores them in database and checks them
 *
 * @author eliseev
 */
public interface AccountManager {
    /**
     * Puts user by email to the system.
     */
    public void putUser(final @Nonnull User user);

    public boolean checkUser(final @Nonnull User user);

    void addWordToUser(final @Nonnull User user, final @Nonnull Word word);

    @Nonnull
    List<Word> getWords(final @Nonnull User user);

    void removerUserWord(final @Nonnull User user, final @Nonnull Word word);
}