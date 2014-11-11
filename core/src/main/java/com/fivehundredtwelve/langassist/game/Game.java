package com.fivehundredtwelve.langassist.game;

import com.fivehundredtwelve.langassist.User;

/**
 * Created by igor on 08.11.14.
 */

/**
 * Represents game essence running in container and responds to players requests.
 * Implements lifecycle methods and request processing method.
 */
public abstract class Game {

    private PlayerManager playerManager;
    private GameDataStorage gameDataStorage;
    private GroupDataStorage groupDataStorage;

    private int gameId;
    private int gameGroupId;
    private Player player;
    private long startTime;
    private String gameResultData;
    private String gameGroupResultData;

    public abstract boolean canPlay(User user);

    public abstract String getGameName();

    public abstract long getMaxTimeToLife();

    public abstract void afterStart(Object startData);

    public abstract void beforeEnd();

    public abstract void afterStartGameGroup(Object startData);

    public abstract void beforeEndGameGroup(PlayerGameResult[] gameResults);

    public abstract Response processRequest(Request request);

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getGameResultData() {
        return gameResultData;
    }

    public void setGameResultData(String gameResultData) {
        this.gameResultData = gameResultData;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public String getGameGroupResultData() {
        return gameGroupResultData;
    }

    public void setGameGroupResultData(String gameGroupResultData) {
        this.gameGroupResultData = gameGroupResultData;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public GameDataStorage getGameDataStorage() {
        return gameDataStorage;
    }

    public void setGameDataStorage(GameDataStorage gameDataStorage) {
        this.gameDataStorage = gameDataStorage;
    }

    public GroupDataStorage getGroupDataStorage() {
        return groupDataStorage;
    }

    public void setGroupDataStorage(GroupDataStorage groupDataStorage) {
        this.groupDataStorage = groupDataStorage;
    }
}
