package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 08.11.14.
 */

/**
 * Stores game group data. Used to store general game group data.
 */
public class GroupDataStorage implements DataStorage<String, String> {

    private int groupId;
    private GameManager gameManager;

    public GroupDataStorage(int groupId, GameManager gameManager) {
        setGroupId(groupId);
        setGameManager(gameManager);
    }

    @Override
    public void put(String key, String value) {

        gameManager.putDataToGroupStorage(getGroupId(), key, value);

    }

    @Override
    public String get(String key) {

        return gameManager.getDataFromGroupStorage(getGroupId(), key);

    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}
