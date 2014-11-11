package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 09.11.14.
 */

/**
 * Represents game related data.
 */
public class GameDescription {

    private int gameId;
    private int gameGroupId;
    private GameStatus status;
    private long gameStartTime;
    private long maxGameTTL;
    private long invitationExpiredTime;
    private String gameName;
    private String gameResultData;
    private String gameGroupResultData;
    private String playerLogin;

    public GameDescription() {
    }

    ;

    public GameDescription(int gameId, int gameGroupId, GameStatus status, String gameResultData, String gameGroupResultData) {
        setGameId(gameId);
        setGameGroupId(gameGroupId);
        setStatus(status);
        setGameResultData(gameResultData);
        setGameGroupResultData(gameGroupResultData);
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameGroupId() {
        return gameGroupId;
    }

    public void setGameGroupId(int gameGroupId) {
        this.gameGroupId = gameGroupId;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public String getGameResultData() {
        return gameResultData;
    }

    public void setGameResultData(String gameResultData) {
        this.gameResultData = gameResultData;
    }

    public Object getGameGroupResultData() {
        return gameGroupResultData;
    }

    public void setGameGroupResultData(String gameGroupResultData) {
        this.gameGroupResultData = gameGroupResultData;
    }

    public String getPlayerLogin() {
        return playerLogin;
    }

    public void setPlayerLogin(String playerLogin) {
        this.playerLogin = playerLogin;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public long getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(long gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public long getMaxGameTTL() {
        return maxGameTTL;
    }

    public void setMaxGameTTL(long maxGameTTL) {
        this.maxGameTTL = maxGameTTL;
    }

    public long getInvitationExpiredTime() {
        return invitationExpiredTime;
    }

    public void setInvitationExpiredTime(long invitationExpiredTime) {
        this.invitationExpiredTime = invitationExpiredTime;
    }
}
