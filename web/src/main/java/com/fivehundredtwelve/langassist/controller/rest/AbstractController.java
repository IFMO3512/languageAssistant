package com.fivehundredtwelve.langassist.controller.rest;

import org.slf4j.Logger;

/**
 * @author eliseev
 */
public abstract class AbstractController {
    protected abstract Logger getLogger();

    protected Container createErrorContainer(Exception ex) {
        getLogger().warn("Exception occurred", ex);

        return new Container(ResponseCode.ERROR, ex.getMessage());
    }

    protected Container createErrorContainer(String description) {
        getLogger().warn("Error occurred", description);

        return new Container(ResponseCode.ERROR, description);
    }

    protected Container createSuccessContainer(String message) {
        getLogger().debug(message);

        return new Container(ResponseCode.OK);
    }

    /**
     * Replace placeholder at message with data and creates a data container.
     */
    protected <T> Container createSuccessContainer(String message, T data) {
        getLogger().debug(message, data);

        return new DataContainer<>(ResponseCode.OK, data);
    }
}
