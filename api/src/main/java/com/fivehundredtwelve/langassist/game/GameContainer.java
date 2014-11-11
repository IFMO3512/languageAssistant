package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 09.11.14.
 */

/**
 * Contains all {Game} instances, which introduces particular user game. It also manages their lifecycle
 * and invokes their request processing methods.
 */
public interface GameContainer {

    /**
     * Starts game group and game for creator by calling appropriate {GameManager} methods, creates instance of {Game}
     * and puts it into games container.
     *
     * @param gameName    type of a game
     * @param playerLogin login of player who starts a game
     * @param opponents   array of opponent's logins
     * @param startData   special data interpreted by a game at the start
     */
    public GameDescription startGame(String gameName, String playerLogin, String[] opponents, String startData,
                                     long invitationExpiredTimePeriod) throws IllegalArgumentException;

    /**
     * Calls when user confirm game invitation.
     *
     * @param gameGroupId id of a game group
     * @param playerLogin login of player who starts a game
     * @param startData   special data interpreted by a game at the start
     */
    public GameDescription startGame(int gameGroupId, String playerLogin, String startData) throws IllegalArgumentException;

    /**
     * Rejects user game invitation. Ends game group if it was the last game in a group.
     *
     * @param gameGroupId id of a game group
     * @param playerLogin login of a player who rejects a game
     */
    public void rejectGameInvitation(int gameGroupId, String playerLogin);

    /**
     * Sends request to game instance.
     *
     * @param userLogin login of player
     * @param request   request for game instance
     * @return response of game instance
     */
    public Response sendRequest(String userLogin, Request request);

    /**
     * Closes all expired games in container and closes expired games invitations and game groups
     */
    public void closeExpiredGamesAndGameGroups();

}
