package com.fivehundredtwelve.langassist.controller.rest;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author eliseev
 */
public class DataContainer<T> extends Container {
    private final T data;

    public DataContainer(final @Nonnull ResponseCode code, final @Nonnull T data) {
        super(code);
        Preconditions.checkNotNull("Data can't be null", data);

        this.data = data;
    }

    public DataContainer(final @Nonnull ResponseCode code, final @Nullable String description, final @Nonnull T data) {
        super(code, description);
        Preconditions.checkNotNull("Data can't be null", data);

        this.data = data;
    }

    public T getData() {
        return data;
    }
}
