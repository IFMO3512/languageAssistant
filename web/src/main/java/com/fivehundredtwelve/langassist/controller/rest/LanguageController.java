package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author anna_mukhina
 */
@RestController
@RequestMapping("/languages")
public class LanguageController extends AbstractController {
    private final static Logger LOGGER = LoggerFactory.getLogger(LanguageController.class);


    @RequestMapping(value = "/getLanguages", method = RequestMethod.GET)
    public Container getLanguages() {

        LOGGER.debug("Getting all languages");
        try {
            return createSuccessContainer("System languages are {}", Language.values());
        } catch (Exception ex) {
            return createErrorContainer(ex);
        }
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
