package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.game.GameContainer;
import com.fivehundredtwelve.langassist.game.GameDescription;
import com.fivehundredtwelve.langassist.game.Request;
import com.fivehundredtwelve.langassist.game.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by igor on 10.11.14.
 */

@RestController
@RequestMapping("/game")
public class GameController extends AbstractController {

    private final static Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameContainer gameContainer;

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Container createGame(@RequestParam(value = "gameName", required = true) String gameName,
                                @RequestParam(value = "opponents", required = true) String[] opponents,
                                @RequestParam(value = "invitationExpiredTimePeriod", required = true)
                                int invitationExpiredTimePeriod,
                                @RequestParam(value = "startData", required = true) String startData,
                                @CookieValue("name") String name,
                                @CookieValue("domain") String domain) {

        String userLogin = name+"@"+domain;

        GameDescription gd = null;

        try {
            gd = gameContainer.startGame(gameName, userLogin, opponents, startData, invitationExpiredTimePeriod);
        } catch (IllegalArgumentException e) {

            LOGGER.warn("Exception occurred because of illegal game name: {}", e);

            return createErrorContainer("Illegal game name.");
        } catch(RuntimeException e) {

            LOGGER.warn("Game group was not created. Exception occurred: {}", e);

            return createErrorContainer(e);
        }


        LOGGER.debug("Game group with id {} and game with id {} were created for user {}.", gd.getGameGroupId(), gd.getGameId(), userLogin);

        return createSuccessContainer("Game was created.", gd);
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public Container confirm(@RequestParam(value = "gameGroupId", required = true) int gameGroupId,
                             @RequestParam(value = "startData", required = true) String startData,
                             @CookieValue("name") String name,
                             @CookieValue("domain") String domain) {

        String userLogin = name+"@"+domain;

        GameDescription gd = null;

        try {
            gd = gameContainer.startGame(gameGroupId, userLogin, startData);
        } catch (IllegalArgumentException e) {

            LOGGER.warn("Exception occurred while confirming game invitation with game group id {}: {}", gameGroupId, e);

            return createErrorContainer(e);

        } catch (RuntimeException e) {

            LOGGER.warn("Exception occurred while confirming game invitation with game group id {}: {}", gameGroupId, e);

            return createErrorContainer(e);
        }

        if (gd == null) {

            LOGGER.warn("Game container returned null.");

            return createErrorContainer("Some error while creation game.");
        }

        LOGGER.debug("Game invitation is confirmed by {}, game id is {}.", userLogin, gd.getGameId());


        return createSuccessContainer("Game invitation is confirmed.", gd);
    }

    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    public Container reject(@RequestParam(value = "gameGroupId", required = true) int gameGroupId,
                            @CookieValue("name") String name,
                            @CookieValue("domain") String domain) {

        String userLogin = name+"@"+domain;

        gameContainer.rejectGameInvitation(gameGroupId, userLogin);

        LOGGER.debug("Game invitation in group id {} was rejected by user {}.", gameGroupId, userLogin);

        return createSuccessContainer("Game invitation was rejected.");
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public Container sendMessage(@RequestParam(value = "request", required = true) Request request,
                                 @CookieValue("name") String name,
                                 @CookieValue("domain") String domain) {

        String userLogin = name+"@"+domain;

        Response response = gameContainer.sendRequest(userLogin, request);

        return createSuccessContainer("Message was sent.", response);
    }
}
