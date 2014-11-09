package com.fivehundredtwelve.langassist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Class that represent the word entity.
 * This class is immutable.
 * <p/>
 * Created by eliseev on 19/09/14.
 */
public class Word implements Serializable {

    private static final long serialVersionUID = -5075958923027437397L;
    private final String word;
    private final Language language;
    private final Word translation;
    private final Category category;

    /**
     * Creates a {@link Word} object with specified email.
     *
     * @param language language, bounded to this word
     * @throws java.lang.NullPointerException if word is null
     */
    public Word(final @Nonnull String word, final @Nonnull Language language) {
        this(word, language, null, null);
    }

    @JsonCreator
    public Word(final @JsonProperty("word") @Nonnull String word,
                final @JsonProperty("language") @Nonnull String language) {
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(language);

        this.word = word;
        this.language = Language.getLanguage(language);
        this.translation = null;
        this.category = null;

        if (this.language == null) throw new IllegalArgumentException("Language not found");
    }

    public Word(final @Nonnull String word, final @Nonnull Language language,
                final @Nullable Word translation, final @Nullable Category category) {
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(language);

        this.word = word;
        this.language = language;
        this.translation = translation;
        this.category = category;
    }

    public Word(final String word, final Language language, final Word translation) {
        this(word, language, translation, null);
    }

    public Language getLanguage() {
        return language;
    }

    public String getWord() {
        return word;
    }

    @Nullable
    public Word getTranslation() {
        return translation;
    }

    @Nullable
    public Category getCategory() {
        return category;
    }

    @Nonnull
    public Word withTranslation(Word translation) {
        return new Word(word, language, translation);
    }

    @Nonnull
    public Word minimal() {
        return new Word(word, language);
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
                .append("translation", translation)
                .append("category", category)
                .toString();
    }
}
