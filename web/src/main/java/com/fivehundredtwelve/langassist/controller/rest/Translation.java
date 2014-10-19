package com.fivehundredtwelve.langassist.controller.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fivehundredtwelve.langassist.Word;
import com.google.common.base.Preconditions;

/**
 * @author eliseev
 */
public class Translation {
    private final Word source;

    private final Word translation;

    @JsonCreator
    public Translation(@JsonProperty("source") final Word source,
                       @JsonProperty("translation") final Word translation) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(translation);

        this.source = source;
        this.translation = translation;
    }

    public Word getSource() {
        return source;
    }

    public Word getTranslation() {
        return translation;
    }
}
