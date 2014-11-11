package com.fivehundredtwelve.langassist.game;

import javax.annotation.Nullable;

/**
 * Created by igor on 10.11.14.
 */
public enum GameStatus {

    RUN((short) 0),
    INVITATION((short) 1),
    ENDED((short) 2),
    REJECTED((short) 3),
    EXPIRED((short) 4); // Sets if status before was 0

    private final short status;

    GameStatus(short status) {
        this.status = status;
    }

    @Nullable
    public static GameStatus getLanguage(short status) {
        for (GameStatus gameStatus : GameStatus.values()) {
            if (gameStatus.status == status) return gameStatus;
        }
        return null;
    }

    public short getStatus() {
        return status;
    }
}
