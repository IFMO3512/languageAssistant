package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 09.11.14.
 */

/**
 * Represents response to a game from player.
 */
public class Response {

    public static final short SUCCESS_CODE = 0;
    public static final short ERROR_CODE = 1;
    public static final short TRY_LATER_CODE = 2;

    private short code;
    private String data;

    public Response(short code, String data) {
        setCode(code);
        setData(data);
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
