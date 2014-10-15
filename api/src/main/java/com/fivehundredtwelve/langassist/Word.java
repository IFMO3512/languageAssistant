package com.fivehundredtwelve.langassist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public Word(final @JsonProperty("word") @Nonnull String word,
                final @JsonProperty("language") @Nonnull String language) {
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

    public String getLanguageName() {
        return language.getLanguageName();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        final Word word1 = (Word) o;

        if (language != word1.language) return false;
        if (!word.equals(word1.word)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + language.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("word", word)
                .append("language", language)
                .toString();
    }
}
