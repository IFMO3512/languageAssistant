package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 08.11.14.
 */

/**
 * Represents request to a game from player.
 */
public class Request {

    private short type;
    private String data;

    public Request(short type, String data) {
        setType(type);
        setData(data);
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
