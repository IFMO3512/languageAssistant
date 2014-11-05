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
public class WordWithTranslation extends Word {


    @JsonCreator
    public WordWithTranslation(final @JsonProperty("word") @Nonnull String word,
                               final @JsonProperty("language") @Nonnull String language,
                               final @JsonProperty("translation") @Nonnull Word translation) {
        super(word, Language.getLanguage(language), translation);

        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(language);
        Preconditions.checkNotNull(translation);

        if (getLanguage() == null) throw new IllegalArgumentException("Language not found");
    }
}
