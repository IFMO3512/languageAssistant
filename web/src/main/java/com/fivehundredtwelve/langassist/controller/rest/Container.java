package com.fivehundredtwelve.langassist.controller.rest;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author eliseev
 */
public class Container {
    private final ResponseCode code;

    private String description;

    protected Container(final @Nonnull ResponseCode code) {
        Preconditions.checkNotNull("Response code can't be null", code);

        this.code = code;

    }

    public Container(final @Nonnull ResponseCode code, final @Nullable String description) {
        Preconditions.checkNotNull("Response code can't be null", code);

        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final @Nullable String description) {
        this.description = description;
    }

    public ResponseCode getCode() {

        return code;
    }
}
