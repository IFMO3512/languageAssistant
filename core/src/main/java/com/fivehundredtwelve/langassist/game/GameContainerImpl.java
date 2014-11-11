package com.fivehundredtwelve.langassist.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by igor on 08.11.14.
 */
public class GameContainerImpl implements GameContainer {

    public static final long DEFAULT_CHECK_EXPIRED_GAMES_TIME_PERIOD = 1000 * 60;
    private long checkExpiredGamesTimePeriod = DEFAULT_CHECK_EXPIRED_GAMES_TIME_PERIOD;
    private final static Logger LOGGER = LoggerFactory.getLogger(GameContainerImpl.class);
    private ConcurrentHashMap<String, GameLifecycleStatus> container;
    private LinkedList<GameLifecycleStatus> list;
    private GameLoader gameLoader;
    private GameManager gameManager;
    private PlayerManager playerManager;
    private Timer checkExpiredGamesTimer;

    public GameContainerImpl() {
        container = new ConcurrentHashMap<String, GameLifecycleStatus>();
        list = new LinkedList<GameLifecycleStatus>();

        // Set timer for periodic checking of expired games
        checkExpiredGamesTimer = new Timer(false);
        checkExpiredGamesTimer.schedule(new CheckExpiredGamesTimerTask(this), getCheckExpiredGamesTimePeriod(), getCheckExpiredGamesTimePeriod());
    }

    /**
     * Calls appropriate game lifecycle methods and {GameManager} methods to end the game.
     *
     * @param game game instance.
     */
    private void endGame(Game game) {
        // Calls game lifecycle method
        game.beforeEnd();

        // Must check if other users is still in game by one request, else could be a blocking
        // Ends game
        PlayerGameResult[] playerGameResult = gameManager.endGame(game.getGameId(), game.getGameResultData());

        LOGGER.debug("A game with id {} was ended.", game.getGameId());

        if (playerGameResult != null) {
            // Calls game group lifecycle method
            game.beforeEndGameGroup(playerGameResult);
            // Ends game group
            gameManager.endGameGroup(game.getGameGroupId(), game.getGameGroupResultData());
            LOGGER.debug("A game group with id {} was ended.", game.getGameGroupId());
        }

    }

    @Override
    public GameDescription startGame(String gameName, String playerLogin, String[] opponents, String startData,
                                     long invitationExpiredTimePeriod) {

        Game game = null;

        // Checks if user already has running game
        GameLifecycleStatus gls = container.get(playerLogin);
        if (gls != null) {
            LOGGER.debug("Running game of user {} is already exists.", playerLogin);

            throw new RuntimeException("User already has running game.");
        }

        // TODO - check user's logins existing

        // Loads new game instance
        try {
            game = gameLoader.loadGame(gameName);
        } catch (IllegalAccessException e) {
            LOGGER.warn("Some error occurred while loading Game {}: {}", gameName, e);
            throw new RuntimeException("Some error occurred.");
        } catch (InstantiationException e) {
            LOGGER.warn("Some error occurred while loading Game {}: {}", gameName, e);
            throw new RuntimeException("Some error occurred.");
        } catch (IllegalArgumentException e) {
            LOGGER.warn("User {} tried to start game with invalid name {}.", playerLogin, gameName);

            throw e;
        }


        // Creates game and game group
        GameDescription gd = gameManager.createGameGroup(gameName, playerLogin, opponents, invitationExpiredTimePeriod);

        // Initializes game data
        game.setGameId(gd.getGameId());
        game.setGameGroupId(gd.getGameGroupId());
        game.setPlayer(playerManager.getPlayer());
        game.setStartTime(new Date().getTime());
        game.setPlayerManager(playerManager);
        game.setGameDataStorage(new GameDataStorage());
        game.setGroupDataStorage(new GroupDataStorage(gd.getGameGroupId(), gameManager));

        // Calls game lifecycle methods
        game.afterStartGameGroup(startData);
        game.afterStart(startData);

        // Puts game in container
        gls = new GameLifecycleStatus(game);
        container.put(playerLogin, gls);
        list.add(gls);

        LOGGER.debug("A game group with id {} was started", game.getGameGroupId());
        LOGGER.debug("A game with id {} was started.", game.getGameId());

        return gd;
    }

    @Override
    public GameDescription startGame(int gameGroupId, String playerLogin, String startData) {

        // Checks if user already has running game
        GameLifecycleStatus gls = container.get(playerLogin);
        if (gls != null) {
            LOGGER.debug("Running game of user {} is already exists.", playerLogin);

            throw new RuntimeException("User already has running game.");
        }

        GameDescription gd = gameManager.getGameGroupDescription(gameGroupId);

        if (gd == null) {
            LOGGER.debug("Game group with id {} does not exists.", gameGroupId);

            throw new IllegalArgumentException("Game group id " + new Integer(gameGroupId).toString() + " is invalid.");
        }

        Game game;

        try {
            game = gameLoader.loadGame(gd.getGameName());
        } catch (IllegalAccessException e) {
            LOGGER.warn("Some error occurred while loading Game {}: {}", gd.getGameName(), e);

            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            LOGGER.warn("Some error occurred while loading Game {}: {}", gd.getGameName(), e);

            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Game name {} is invalid.", gd.getGameName());

            throw new RuntimeException("Error while loading game.");
        }


        gd = gameManager.confirmGameInvitation(gameGroupId, playerLogin);

        // No invitations were found with such game group id
        if (gd == null) {
            LOGGER.warn("No invitations with game group id {} for user {}.", gameGroupId, playerLogin);

            throw new IllegalArgumentException("No invitations with game group id " + new Integer(gameGroupId).toString() + " for user " + playerLogin + ".");
        }

        // Initializes game data
        game.setGameId(gd.getGameId());
        game.setGameGroupId(gd.getGameGroupId());
        game.setPlayer(playerManager.getPlayer());
        game.setStartTime(new Date().getTime());
        game.setPlayerManager(playerManager);
        game.setGameDataStorage(new GameDataStorage());
        game.setGroupDataStorage(new GroupDataStorage(gd.getGameGroupId(), gameManager));

        // Calls game lifecycle methods
        game.afterStart(startData);

        // Puts game in container
        gls = new GameLifecycleStatus(game);
        container.put(playerLogin, gls);
        list.add(gls);

        LOGGER.debug("A game with id {} was started.", game.getGameId());

        return gd;
    }

    @Override
    public void rejectGameInvitation(int gameGroupId, String playerLogin) {

        PlayerGameResult[] pgr = null;

        try{
            pgr = gameManager.rejectGameInvitation(gameGroupId, playerLogin);
        } catch(RuntimeException e) {
            LOGGER.debug("No game invitation for user {} with game group id {}.", playerLogin, gameGroupId);
        }

        if (pgr != null) {

            // Game group was ended right now

            GameDescription gd = gameManager.getGameGroupDescription(gameGroupId);

            try {
                Game game = gameLoader.loadGame(gd.getGameName());

                game.beforeEndGameGroup(pgr);

                // Ends game group
                gameManager.endGameGroup(gd.getGameGroupId(), game.getGameGroupResultData());

            } catch (IllegalAccessException e) {
                LOGGER.warn("Exception while loading a game with name {}: {}", gd.getGameName(), e);
            } catch (InstantiationException e) {
                LOGGER.warn("Exception while loading a game with name {}: {}", gd.getGameName(), e);
            } catch(IllegalArgumentException e) {
                LOGGER.warn("Game name {} is invalid.", gd.getGameName());
            }
        }
    }

    @Override
    public Response sendRequest(String userLogin, Request request) {

        GameLifecycleStatus gls = container.get(userLogin);

        if (gls == null) {
            // No game of that user is in container

            LOGGER.debug("No game of user {} is in container.", userLogin);

            return new Response(Response.ERROR_CODE, null);
        }

        LOGGER.debug("Request to game with id {} is received.", gls.getGame().getGameId());

        // Synchronization for processing requests
        synchronized (gls) {

            if (gls.getLifecycleStatus() == LifecycleStatus.RUN && gls.getGame().getMaxTimeToLife() + gls.getGame().getStartTime() >= new Date().getTime()) {
                // Game is expired
                list.remove(gls);
                container.remove(userLogin);
                gls.setLifecycleStatus(LifecycleStatus.ENDED);

                // Ends game
                endGame(gls.getGame());

                LOGGER.debug("Game with id {} is expired and was closed.", gls.getGame().getGameGroupId());
            }

            if (gls.getLifecycleStatus() == LifecycleStatus.RUN) {

                Response response = gls.getGame().processRequest(request);

                LOGGER.debug("Request to game with id {} was processed.", gls.getGame().getGameId());

                return response;

            } else if (gls.getLifecycleStatus() == LifecycleStatus.STOPED) {

                return new Response(Response.TRY_LATER_CODE, null);

            } else if (gls.getLifecycleStatus() == LifecycleStatus.ENDED) {

                return new Response(Response.ERROR_CODE, null);

            } else return new Response(Response.ERROR_CODE, null);

        }

    }

    @Override
    public void closeExpiredGamesAndGameGroups() {

        // Checks expired games in container
        for (GameLifecycleStatus gls : list) {

            if (gls.getLifecycleStatus() == LifecycleStatus.RUN && gls.getGame().getMaxTimeToLife() + gls.getGame().getStartTime() >= new Date().getTime()) {

                synchronized (gls) {
                    // Game is expired
                    if (gls.getLifecycleStatus() == LifecycleStatus.RUN) {
                        list.remove(gls);
                        container.remove(gls.getGame().getPlayer().getLogin());
                        gls.setLifecycleStatus(LifecycleStatus.ENDED);

                        LOGGER.debug("Game with id {} expired.", gls.getGame().getGameId());

                        // Ends game
                        endGame(gls.getGame());
                    }
                }

            }

        }

        // Checks expired game invitations and processes ended game groups
        GameDescription[] groupDescriptions = gameManager.closeExpiredGamesEndReturnExpiredGameGroups();

        for (GameDescription gd : groupDescriptions) {
            try {
                // Closes expired game group

                Game game = gameLoader.loadGame(gd.getGameName());

                PlayerGameResult[] pgr = gameManager.getPlayersGameResults(gd.getGameGroupId());

                game.beforeEndGameGroup(pgr);

                gameManager.endGameGroup(gd.getGameGroupId(), game.getGameGroupResultData());

                LOGGER.debug("A game group with id {} was ended.", gd.getGameGroupId());

            } catch (IllegalAccessException e) {
                LOGGER.warn("Exception while loading a game with name {}: {}", gd.getGameName(), e);
            } catch (InstantiationException e) {
                LOGGER.warn("Exception while loading a game with name {}: {}", gd.getGameName(), e);
            } catch(IllegalArgumentException e) {
                LOGGER.warn("Game name {} is invalid.", gd.getGameName());
            }
        }

    }

    public GameLoader getGameLoader() {
        return gameLoader;
    }

    public void setGameLoader(GameLoader gameLoader) {
        this.gameLoader = gameLoader;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public long getCheckExpiredGamesTimePeriod() {
        return checkExpiredGamesTimePeriod;
    }

    public void setCheckExpiredGamesTimePeriod(long checkExpiredGamesTimePeriod) {
        this.checkExpiredGamesTimePeriod = checkExpiredGamesTimePeriod;
    }

    public static class CheckExpiredGamesTimerTask extends TimerTask {

        private GameContainerImpl gc;

        public CheckExpiredGamesTimerTask(GameContainerImpl gc) {
            this.gc = gc;
        }

        public void run() {
            gc.closeExpiredGamesAndGameGroups();
        }

    }
}
