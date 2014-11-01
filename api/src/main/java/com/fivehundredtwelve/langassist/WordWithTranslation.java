package com.fivehundredtwelve.langassist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * @author eliseev
 */
public class WordWithTranslation implements Serializable {
    private static final long serialVersionUID = 912393284l;

    private final Word source;

    private final Word translation;

    @JsonCreator
    public WordWithTranslation(@JsonProperty("source") final @Nonnull Word source,
                               @JsonProperty("translation") final @Nonnull Word translation) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(translation);

        this.source = source;
        this.translation = translation;
    }

    /**
     * <b>Only for local usage</b>. Version of object without translation returns null.
     */
    public WordWithTranslation(final @Nonnull Word source) {
        Preconditions.checkNotNull(source);

        this.source = source;
        this.translation = null;
    }

    @Nonnull
    public Word getSource() {
        return source;
    }

    @Nullable
    public Word getTranslation() {
        return translation;
    }
}
