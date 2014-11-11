package com.fivehundredtwelve.langassist.game;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by igor on 09.11.14.
 */
public class GameManagerImpl implements GameManager {

    public static final long maxGameTTL = 600000;

    private final static Logger LOGGER = LoggerFactory.getLogger(GameManagerImpl.class);
    private ConcurrentHashMap<Integer, GameDescription> games = new ConcurrentHashMap<Integer, GameDescription>();
    private ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> gameGroupStorage
            = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>>();

    private volatile int lastGameId = 0;

    @Override
    public GameDescription createGameGroup(@Nonnull String gameName,
                                           @Nonnull String creatorLogin,
                                           @Nullable String[] opponentsLogins,
                                           long invitationExpiredTimeInterval) {
        // Data validation
        Preconditions.checkNotNull(gameName, "Game name must not be null.");
        Preconditions.checkNotNull(creatorLogin, "Creator login must not be null.");
        Preconditions.checkArgument(invitationExpiredTimeInterval >= 0, "Invitation expired time interval mut not be negative.");

        GameDescription gameDescription = new GameDescription();

        long currentTime = new Date().getTime();
        int gameId = lastGameId++;
        int gameGroupId = gameId;

        // Initializing game data
        gameDescription.setGameId(gameId);
        gameDescription.setGameGroupId(gameGroupId);
        gameDescription.setPlayerLogin(creatorLogin);
        gameDescription.setGameName(gameName);
        gameDescription.setStatus(GameStatus.RUN);
        gameDescription.setMaxGameTTL(maxGameTTL);

        games.put(gameId, gameDescription);

        if (opponentsLogins != null) {
            for (String playerLogin : opponentsLogins) {

                // TODO - check players login existing

                gameId = lastGameId++;

                GameDescription gd = new GameDescription();
                // Initializing game data
                gd.setGameId(gameId);
                gd.setGameGroupId(gameGroupId);
                gd.setPlayerLogin(playerLogin);
                gd.setGameName(gameName);
                gd.setStatus(GameStatus.INVITATION);
                gd.setInvitationExpiredTime(currentTime + invitationExpiredTimeInterval);
                gd.setGameStartTime(currentTime);
                games.put(gameId, gd);

                LOGGER.debug("A game invitation with id {} was created.", gd.getGameId());
            }

        }

        gameGroupStorage.put(gameGroupId, new ConcurrentHashMap<String, String>());

        LOGGER.debug("A game group with id {} was started", gameDescription.getGameGroupId());
        LOGGER.debug("A game with id {} was started.", gameDescription.getGameId());

        return gameDescription;
    }

    @Override
    public GameDescription getUserGame(@Nonnull String playerLogin) {

        // Data validation
        Preconditions.checkNotNull(playerLogin, "Player login must not be null.");

        GameDescription gd;
        for (Integer gameId : games.keySet()) {
            gd = games.get(gameId);
            if (gd.getPlayerLogin().equals(playerLogin) && gd.getStatus() == GameStatus.RUN) {
                LOGGER.debug("Current user {} game was returned.", playerLogin);
                return gd;
            }
        }

        LOGGER.debug("User {} does not have running games. ", playerLogin);

        return null;
    }

    @Override
    public GameDescription[] getUserGameInvitations(@Nonnull String playerLogin) {

        // Data validation
        Preconditions.checkNotNull(playerLogin, "Player login must not be null.");

        LinkedList<GameDescription> gdList = new LinkedList<GameDescription>();
        GameDescription gd;
        for (Integer gameId : games.keySet()) {
            gd = games.get(gameId);
            if (gd.getPlayerLogin().equals(playerLogin) && gd.getStatus() == GameStatus.INVITATION) gdList.add(gd);
        }

        LOGGER.debug("Current user {} game invitations were returned.", playerLogin);

        return gdList.toArray(new GameDescription[gdList.size()]);
    }

    @Override
    public GameDescription confirmGameInvitation(int gameGroupId, @Nonnull String playerLogin) {

        // Data validation
        Preconditions.checkArgument(gameGroupId >= 0, "Game group id must not be negative.");
        Preconditions.checkNotNull(playerLogin, "Player login must not be null.");

        GameDescription gd;
        for (Integer gameId : games.keySet()) {
            gd = games.get(gameId);
            if (gd.getPlayerLogin().equals(playerLogin) && gd.getStatus() == GameStatus.INVITATION && gd.getGameGroupId() == gameGroupId) {
                gd.setStatus(GameStatus.RUN);
                gd.setGameStartTime(new Date().getTime());

                LOGGER.debug("User {} game invitation with id {} was confirmed.", playerLogin, gd.getGameId());

                return gd;
            }

        }

        LOGGER.debug("No game invitation for user {} with game group id {} was found.", playerLogin, gameGroupId);

        return null;
    }

    @Override
    public PlayerGameResult[] rejectGameInvitation(int gameGroupId, @Nonnull String playerLogin) {

        // Data validation
        Preconditions.checkArgument(gameGroupId >= 0, "Game group id must not be negative.");
        Preconditions.checkNotNull(playerLogin, "Player login must not be null.");

        LinkedList<PlayerGameResult> pgrList = new LinkedList<PlayerGameResult>();

        // Marks game as rejected
        boolean gameRejectedFlag = false;
        GameDescription gd;
        for (Integer gameId : games.keySet()) {
            gd = games.get(gameId);
            if (gd.getPlayerLogin().equals(playerLogin) && gd.getStatus() == GameStatus.INVITATION && gd.getGameGroupId() == gameGroupId) {

                gd.setStatus(GameStatus.REJECTED);

                gameRejectedFlag = true;

                LOGGER.debug("User's {} invitation with game group id {} was rejected.", playerLogin, gameGroupId);

                break;
            }

        }

        if (!gameRejectedFlag) {

            LOGGER.debug("User {} does not have invitations with game group id {} to reject.", playerLogin, gameGroupId);

            throw new RuntimeException("User does not have invitation with such game group id.");
        }

        // Returns PlayerGameResult array if game group was ended
        boolean gameGroupEndedFlag = true;
        for (Integer gameId : games.keySet()) {
            gd = games.get(gameId);
            if (gd.getGameGroupId() == gameGroupId) {
                if (!(gd.getStatus() == GameStatus.ENDED || gd.getStatus() == GameStatus.REJECTED)) {
                    gameGroupEndedFlag = false;
                    break;
                }
                pgrList.add(new PlayerGameResult(gd.getPlayerLogin(), gd.getGameResultData()));
            }
        }

        return (gameGroupEndedFlag) ? pgrList.toArray(new PlayerGameResult[pgrList.size()]) : null;
    }

    @Override
    public GameDescription[] getGroupGames(int gameGroupId) {

        // Data validation
        Preconditions.checkArgument(gameGroupId >= 0, "Game group id must not be negative.");

        GameDescription gd = games.get(gameGroupId);

        if (gd == null) {
            LOGGER.debug("No game group with id {} exists.", gameGroupId);

            return null;
        }

        LinkedList<GameDescription> gdList = new LinkedList<GameDescription>();

        for (Integer gameId : games.keySet()) {
            gd = games.get(gameId);
            if (gd.getGameGroupId() == gameGroupId) {
                gdList.add(gd);
            }
        }

        LOGGER.debug("Games of game group with id {} were returned.", gameGroupId);

        return gdList.toArray(new GameDescription[gdList.size()]);
    }

    @Override
    public PlayerGameResult[] endGame(int gameId, @Nonnull String gameResultData) {

        // Data validation
        Preconditions.checkArgument(gameId >= 0, "Game id must not be negative.");

        LinkedList<PlayerGameResult> pgrList = new LinkedList<PlayerGameResult>();

        // Marks game as ended and sets game result object
        GameDescription gd = games.get(gameId);

        if (gd != null) {

            gd.setStatus(GameStatus.ENDED);
            gd.setGameResultData(gameResultData);

            LOGGER.debug("Games with id {} was ended.", gameId);

            int gameGroupId = gd.getGameGroupId();

            // Returns PlayerGameResult array if game group was ended
            boolean gameGroupEndedFlag = true;
            for (Integer _gameId : games.keySet()) {
                gd = games.get(_gameId);
                if (gd.getGameGroupId() == gameGroupId) {
                    if (!(gd.getStatus() == GameStatus.ENDED || gd.getStatus() == GameStatus.REJECTED)) {
                        gameGroupEndedFlag = false;
                        break;
                    }
                    pgrList.add(new PlayerGameResult(gd.getPlayerLogin(), gd.getGameResultData()));
                }
            }

            return (gameGroupEndedFlag) ? pgrList.toArray(new PlayerGameResult[pgrList.size()]) : null;
        }

        LOGGER.debug("Games with id {} was not found.", gameId);

        return null;
    }

    @Override
    public void endGameGroup(int gameGroupId, @Nonnull String gameGroupResultData) {

        // Data validation
        Preconditions.checkArgument(gameGroupId >= 0, "Game group id must not be negative.");

        GameDescription gd = games.get(gameGroupId);

        if (gd == null) {
            LOGGER.debug("Game group with id {} does not exists.", gameGroupId);

            return;
        }

        //boolean gameGroupEndedFlag = true;
        for (Integer gameId : games.keySet()) {
            gd = games.get(gameId);
            if (gd.getGameGroupId() == gameGroupId) {
                gd.setGameGroupResultData(gameGroupResultData);
            }
        }

        LOGGER.debug("Game group with id {} was ended.", gameGroupId);

        // Clears game group storage
        gameGroupStorage.remove(gameGroupId);
    }

    @Override
    public GameDescription getGameGroupDescription(int gameGroupId) {

        // Data validation
        Preconditions.checkArgument(gameGroupId >= 0, "Game group id must not be negative.");

        GameDescription gd = games.get(gameGroupId);

        if (gd != null) {
            LOGGER.debug("Games group description for game group with id {} returned.", gameGroupId);
        } else {
            LOGGER.debug("Games group with id {} was not found.", gameGroupId);
        }

        return gd;

    }

    @Override
    public GameDescription getGameDescription(int gameId) {

        // Data validation
        Preconditions.checkArgument(gameId >= 0, "Game id must not be negative.");

        GameDescription gd = games.get(gameId);

        if (gd != null) {
            LOGGER.debug("Games description for game with id {} returned.", gameId);
        } else {
            LOGGER.debug("Games with id {} was not found.", gameId);
        }

        return gd;

    }

    @Override
    public PlayerGameResult[] getPlayersGameResults(int gameGroupId) {

        // Data validation
        Preconditions.checkArgument(gameGroupId >= 0, "Game group id must not be negative.");

        LinkedList<PlayerGameResult> pgrList = new LinkedList<PlayerGameResult>();

        GameDescription gd = games.get(gameGroupId);

        if (gd != null) {

            for (Integer gameId : games.keySet()) {
                gd = games.get(gameId);
                if (gd.getGameGroupId() == gameGroupId) {
                    pgrList.add(new PlayerGameResult(gd.getPlayerLogin(), gd.getGameResultData()));
                }
            }

            LOGGER.debug("Players results for game group with id {} returned.", gameGroupId);

            return pgrList.toArray(new PlayerGameResult[pgrList.size()]);

        } else {

            LOGGER.debug("Games group with id {} was not found.", gameGroupId);

            return null;
        }

    }

    @Override
    public GameDescription[] closeExpiredGamesEndReturnExpiredGameGroups() {

        LinkedList<GameDescription> gdList = new LinkedList<GameDescription>();
        LinkedList<Integer> gameGroupIds = new LinkedList<Integer>();

        // Gets all game groups ids
        GameDescription gd;
        for (Integer gameId : games.keySet()) {
            gd = games.get(gameId);
            if (gd.getGameGroupId() == gd.getGameId()) {
                gameGroupIds.add(gd.getGameGroupId());
            }
        }

        long currentTime = new Date().getTime();

        // Checks if game is expired and sets it as expired and marks it as ended, if all games in a game group
        // is ended - adds it description in a list
        for (Integer ggId : gameGroupIds) {
            boolean gameGroupExpiredFlag = true;
            for (Integer gameId : games.keySet()) {
                gd = games.get(gameId);
                if (gd.getGameGroupId() == ggId) {
                    if ((gd.getStatus() == GameStatus.RUN || gd.getStatus() == GameStatus.INVITATION) && gd.getGameStartTime() + gd.getMaxGameTTL() >= currentTime) {
                        if (gd.getStatus() == GameStatus.RUN) gd.setStatus(GameStatus.EXPIRED);
                        else if (gd.getStatus() == GameStatus.INVITATION) gd.setStatus(GameStatus.REJECTED);

                    } else if (gd.getGameStartTime() + gd.getMaxGameTTL() < currentTime) gameGroupExpiredFlag = false;
                }
            }

            if (gameGroupExpiredFlag) {
                gdList.add(games.get(ggId));
            }
        }

        LOGGER.debug("Expired games were checked.");

        return gdList.toArray(new GameDescription[gdList.size()]);
    }

    @Override
    public void putDataToGroupStorage(int gameGroupId, @Nonnull String key, @Nonnull String value) {

        // Data validation
        Preconditions.checkArgument(gameGroupId >= 0, "Game group id must not be negative.");
        Preconditions.checkNotNull(key, "Data key must not be null.");
        Preconditions.checkNotNull(value, "Data value must not be null.");

        ConcurrentHashMap<String, String> ggs = gameGroupStorage.get(gameGroupStorage);

        if (ggs == null) {

            LOGGER.debug("No game group storage for game group with id {}.", gameGroupId);

            return;
        }

        LOGGER.debug("Data {} : {} was added to game group storage of game group with id {}.", key, value, gameGroupId);

        ggs.put(key, value);
    }

    @Override
    public String getDataFromGroupStorage(int gameGroupId, @Nonnull String key) {

        Preconditions.checkArgument(gameGroupId >= 0, "Game group id must not be negative.");
        Preconditions.checkNotNull(key, "Data key must not be null.");

        ConcurrentHashMap<String, String> ggs = gameGroupStorage.get(gameGroupStorage);

        if (ggs == null) {

            LOGGER.debug("No game group storage for game group with id {}.", gameGroupId);

            return null;
        }

        String value = ggs.get(key);

        LOGGER.debug("Data {} : {} was returned from game group storage of game group with id {}.", key, value, gameGroupId);

        return value;

    }
}
