package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 08.11.14.
 */

/**
 * Represents particular player.
 */
public class Player {

    private int id;
    private String login;

    public Player() {
    }

    public Player(int id, String login) {
        setId(id);
        setLogin(login);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
