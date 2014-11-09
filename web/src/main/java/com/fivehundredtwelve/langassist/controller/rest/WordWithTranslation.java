package com.fivehundredtwelve.langassist.controller.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

/**
 * @author eliseev
 */
public class WordWithTranslation  {

    private Word word;

    @JsonCreator
    public WordWithTranslation(final @JsonProperty("word") @Nonnull String word,
                               final @JsonProperty("language") @Nonnull String language,
                               final @JsonProperty("translation") @Nonnull Word translation) {
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(language);
        Preconditions.checkNotNull(translation);

        Language lang = Language.getLanguage(language);

        if (lang == null) throw new IllegalArgumentException("Language not found");

        this.word = new Word(word, lang, translation);
    }

    public Word getWord() {
        return word;
    }
}
