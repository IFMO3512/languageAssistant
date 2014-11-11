package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 09.11.14.
 */

/**
 * Performs actions to manage game and game group life.
 */
public interface GameManager {

    /**
     * Creates game group and game of creator
     *
     * @param gameName        game type
     * @param creatorLogin    login of creator
     * @param opponentsLogins logins of opponents
     * @return {GameDescription} of new game of creator
     */
    public GameDescription createGameGroup( String gameName,
                                           String creatorLogin,
                                           String[] opponentsLogins,
                                           long invitationExpiredTimeInterval);

    /**
     * Returns current user's game, if error was occurred and it is several running games of single user, it returns
     * the last one.
     *
     * @param playerLogin login of player
     * @return description of user's game
     */
    public GameDescription getUserGame(String playerLogin);

    /**
     * Returns an array of games represents user's invitations.
     *
     * @param playerLogin login of player
     * @return array of games represents user's invitations
     */
    public GameDescription[] getUserGameInvitations(String playerLogin);

    /**
     * Confirms game invitation.
     *
     * @param gameGroupId game group id
     * @param playerLogin login of player
     * @return {GameDescription} represents confirmed game
     */
    public GameDescription confirmGameInvitation(int gameGroupId, String playerLogin);

    /**
     * Rejects game invitation.
     *
     * @param gameGroupId game group id
     * @param playerLogin login of player
     * @return null if rejected game was not the last in group and {PlayerGameResult} array if it was the last
     */
    public PlayerGameResult[] rejectGameInvitation(int gameGroupId, String playerLogin);

    /**
     * Returns games of that group.
     *
     * @param gameGroupId game group id
     * @return {GameDescription} array represents games of this group
     */
    public GameDescription[] getGroupGames(int gameGroupId);

    /**
     * Ends game and sets game result data.
     *
     * @param gameId         game id
     * @param gameResultData game result data
     * @return null if ended game was not the last in group and {PlayerGameResult} array if it was the last
     */
    public PlayerGameResult[] endGame(int gameId, String gameResultData);

    /**
     * Ends game group. Sets game group result data to all games in a group.
     *
     * @param gameGroupId         game group id
     * @param gameGroupResultData game group result data
     */
    public void endGameGroup(int gameGroupId, String gameGroupResultData);

    /**
     * Returns description of a game group.
     *
     * @param gameGroupId game group id
     * @return {GameDescription} object contains group data, other properties, related to particular game are not set
     */
    public GameDescription getGameGroupDescription(int gameGroupId);

    /**
     * Returns game description.
     *
     * @param gameId game id
     * @return {GameDescription} object contains game data
     */
    public GameDescription getGameDescription(int gameId);

    /**
     * Returns array of {PlayerGameResult} objects represents player's game results of that group.
     *
     * @param gameGroupId game group id
     * @return array of {PlayerGameResult} objects represents player's game results of that group
     */
    public PlayerGameResult[] getPlayersGameResults(int gameGroupId);

    /**
     * Closes expired games and returns array of {GameDescription} objects which represents game groups which
     * were ended with closing of expired game invitations.
     *
     * @return array of {GameDescription} objects which represents ended game groups
     */
    public GameDescription[] closeExpiredGamesEndReturnExpiredGameGroups();

    /**
     * Puts data to game group storage.
     *
     * @param gameGroupId game group id
     * @param key         key
     * @param value       value
     */
    public void putDataToGroupStorage(int gameGroupId, String key, String value);

    /**
     * Gets data from game group storage.
     *
     * @param gameGroupId game group id
     * @param key         key
     * @return value
     */
    public String getDataFromGroupStorage(int gameGroupId, String key);

}
