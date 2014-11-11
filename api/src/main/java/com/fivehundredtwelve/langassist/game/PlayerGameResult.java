package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 09.11.14.
 */
public class PlayerGameResult {

    private String playerLogin;
    private String gameResult;

    public PlayerGameResult(String playerLogin, String gameResult) {
        setPlayerLogin(playerLogin);
        setGameResult(gameResult);
    }

    public String getPlayerLogin() {
        return playerLogin;
    }

    public void setPlayerLogin(String playerLogin) {
        this.playerLogin = playerLogin;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }
}
