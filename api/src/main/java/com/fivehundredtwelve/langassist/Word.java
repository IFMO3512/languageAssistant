package com.fivehundredtwelve.langassist;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class that represent the word entity.
 * This class is immutable.
 * <p>
 * Created by eliseev on 19/09/14.
 */
public class Word {
    private final String word;

    /**
     * Creates a {@link Word} object with specified email.
     *
     * @param word string of user email, can not be null
     * @throws java.lang.NullPointerException if word is null
     */
    public Word(String word) {
        Preconditions.checkNotNull(word);

        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        Word word1 = (Word) o;

        if (!word.equals(word1.word)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("word", word)
                .toString();
    }
}
