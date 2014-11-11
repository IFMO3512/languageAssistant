package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 08.11.14.
 */

/**
 * Container for {Game} and {LifecycleStatus}.
 */
public class GameLifecycleStatus {

    private Game game;
    private LifecycleStatus lifecycleStatus;

    public GameLifecycleStatus(Game game) {
        setGame(game);
        setLifecycleStatus(LifecycleStatus.RUN);
    }

    public GameLifecycleStatus(Game game, LifecycleStatus lifecycleStatus) {
        setGame(game);
        setLifecycleStatus(lifecycleStatus);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LifecycleStatus getLifecycleStatus() {
        return lifecycleStatus;
    }

    public void setLifecycleStatus(LifecycleStatus lifecycleStatus) {
        this.lifecycleStatus = lifecycleStatus;
    }
}
