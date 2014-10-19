package com.fivehundredtwelve.langassist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.io.Serializable;

/**
 * Class that represents User with.
 * This class is immutable.
 * <p>
 * Created by eliseev on 19/09/14.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1373264240454974404L;
    private final String email;

    /**
     * Creates a {@link User} object with specified email.
     *
     * @param email string of user email, can not be null
     * @throws java.lang.NullPointerException if email is null
     */
    @JsonCreator
    public User(@JsonProperty("email") String email) {
        Preconditions.checkNotNull(email);

        this.email = email;
    }

    public String getEmail() {
        return email;
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
