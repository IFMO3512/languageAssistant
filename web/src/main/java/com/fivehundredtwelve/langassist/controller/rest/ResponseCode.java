package com.fivehundredtwelve.langassist.controller.rest;

/**
 * @author eliseev
 */
public enum ResponseCode {
    OK(0), ERROR(1), NO_TRANSLATION(2), ILLEGAL_ARGUMENTS(3);

    private final int code;

    ResponseCode(final int code) {

        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
