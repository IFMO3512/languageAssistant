package com.fivehundredtwelve.langassist.controller.rest;

import org.slf4j.Logger;

import javax.annotation.Nonnull;

/**
 * @author eliseev
 */
public abstract class AbstractController {
    @Nonnull
    protected abstract Logger getLogger();

    protected final Container createErrorContainer(final Exception ex) {
        getLogger().warn("Exception occurred", ex);

        return new Container(ResponseCode.ERROR, ex.getMessage());
    }

    protected final Container createSuccessContainer(final String message) {
        getLogger().debug(message);

        return new Container(ResponseCode.OK);
    }

    /**
     * Replace placeholder at message with data and creates a data container.
     */
    protected final <T> Container createSuccessContainer(final String message, final T data) {
        getLogger().debug(message, data);

        return new DataContainer<>(ResponseCode.OK, data);
    }

    protected final Container illegalArgumentsContainer(final String message) {
        getLogger().debug(message);

        return new Container(ResponseCode.ILLEGAL_ARGUMENTS, message);
    }

    protected final Container createFailedContainer(final String why) {
        getLogger().debug("Failed because of {}", why);

        return new Container(ResponseCode.NOT_OK, why);
    }

    protected final String getEmail(final String name, final String domain) {
        return String.format("%s@%s", name, domain);
    }
}
