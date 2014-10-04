package com.fivehundredtwelve.langassist.controller.rest;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author eliseev
 */
public class DataContainer<T> extends Container {
    private final T data;

    private final ResponseCode code;

    private String description;

    public DataContainer(final @Nonnull ResponseCode code, final @Nonnull T data) {
        super(code);
        Preconditions.checkNotNull(data);

        this.data = data;
        this.code = code;
    }

    public DataContainer(final @Nonnull ResponseCode code, final @Nullable String description, final @Nonnull T data) {
        super(code, description);
        Preconditions.checkNotNull(data);

        this.data = data;
        this.code = code;
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public ResponseCode getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final @Nullable String description) {
        this.description = description;
    }
}
