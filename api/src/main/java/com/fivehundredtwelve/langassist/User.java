package com.fivehundredtwelve.langassist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.io.Serializable;

/**
 * Class that represents User with.
 * This class is immutable.
 * <p/>
 * Created by eliseev on 19/09/14.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1373264240454974404L;

    private final String email;
    private final Language language;

    /**
     * Creates a {@link User} object with specified email.
     *
     * @param email string of user email, can not be null
     * @throws java.lang.NullPointerException if email is null
     */
    @JsonCreator
    public User(final @JsonProperty("email") String email) {
        Preconditions.checkNotNull(email);

        this.email = email;
        this.language = Language.RUSSIAN;   // TODO guess user language by getLocale()
    }

    public User(final String email, final Language language) {
        Preconditions.checkNotNull(email);
        Preconditions.checkNotNull(language);

        this.email = email;
        this.language = language;
    }

    public String getEmail() {
        return email;
    }

    public Language getLanguage() {
        return language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!email.equals(user.email)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
