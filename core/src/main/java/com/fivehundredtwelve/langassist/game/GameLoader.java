package com.fivehundredtwelve.langassist.game;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;

/**
 * Created by igor on 09.11.14.
 */

/**
 * Contains list of {Game}s and loads it by their names.
 */
public class GameLoader {

    private final static Logger LOGGER = LoggerFactory.getLogger(GameLoader.class);

    private HashMap<String, Class> gameClasses;

    public GameLoader() {
        gameClasses = new HashMap<String, Class>();
    }

    /**
     * Creates instance of a game by it's name.
     *
     * @param gameName type of a game
     * @return created game instance
     */
    public Game loadGame(@Nonnull String gameName) throws IllegalAccessException, InstantiationException {

        // Data validation
        Preconditions.checkNotNull(gameName, "Game name must not be null.");

        try {

            Class gameClass = gameClasses.get(gameName);

            if (gameClass == null) {

                LOGGER.debug("Not existing game with name {} was requested.", gameName);

                throw new IllegalArgumentException("Game with name " + gameName + " does not exists.");
            }

            Object instance = gameClass.newInstance();

            if (!(instance instanceof Game)) throw new InstantiationException("Class is not Game");

            LOGGER.debug("Game with name {} was instantiated.", gameName);

            return (Game) instance;

        } catch (Exception e) {

            LOGGER.debug("Exception occurred while instantiation Game: {}", e);

            throw e;
        }
    }

    public HashMap<String, Class> getGameClasses() {
        return gameClasses;
    }

    public void setGameClasses(HashMap<String, Class> gameClasses) {
        this.gameClasses = gameClasses;
    }
}
