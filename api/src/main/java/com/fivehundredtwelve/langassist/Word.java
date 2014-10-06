package com.fivehundredtwelve.langassist;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Class that represent the word entity.
 * This class is immutable.
 * <p>
 * Created by eliseev on 19/09/14.
 */
public class Word implements Serializable {

    private static final long serialVersionUID = -5075958923027437396L;
    private final String word;
    private final Language language;

    /**
     * Creates a {@link Word} object with specified email.
     *
     * @param language language, bounded to this word
     * @throws java.lang.NullPointerException if word is null
     */
    public Word(final @Nonnull String word, final @Nonnull Language language) {
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(language);

        this.language = language;
        this.word = word;
    }

    public Word(final @Nonnull String word, final @Nonnull String language) {
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(language);

        this.word = word;
        this.language = Language.getLanguage(language);
    }

    public Language getLanguage() {
        return language;
    }

    public String getWord() {
        return word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        Word word1 = (Word) o;

        return word.equals(word1.word);

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
