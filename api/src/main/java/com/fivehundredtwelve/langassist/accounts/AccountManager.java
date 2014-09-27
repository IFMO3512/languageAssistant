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
    public void addUser(final @Nonnull User user);

    public boolean checkUser(final @Nonnull User user);

    void addTranslationToUser(final @Nonnull User user, final @Nonnull Word word, final @Nonnull Word translation);

    @Nonnull
    List<Word> getTranslations(@Nonnull User user, @Nonnull Word word);
}
