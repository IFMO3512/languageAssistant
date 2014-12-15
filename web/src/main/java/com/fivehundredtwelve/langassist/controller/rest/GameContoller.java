package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.accounts.AccountManager;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author eliseev
 */
@RestController
@RequestMapping("/game")
public class GameContoller extends AbstractController {
    private final Logger LOGGER = LoggerFactory.getLogger(GameContoller.class);

    private final Random random = new Random();

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private DictionaryManager dictionaryManager;

    @RequestMapping("/quests")
    public Container getQuests(final @CookieValue("name") String name, final @CookieValue("domain") String domain) {
        final String email = getEmail(name, domain);

        LOGGER.debug("Creating a questions for user with email={}", email);

        final Collection<Quest> quests = new HashSet<>();

        final Language language = Language.RUSSIAN;
        final Collection<Word> words = dictionaryManager.getWordsWithTranslation(
                accountManager.getWords(new User(email)), language);

        final List<Word> translations = new ArrayList<>(dictionaryManager.getWords());
        translations.removeIf(word -> !word.getLanguage().equals(language));

        if (translations.size() < 5) {
            return createFailedContainer("Need more translations to start game !!!");
        }

        for (final Word word : words) {
            if (word.getTranslation() != null) {
                final Set<Integer> blockedIndexes = new HashSet<>();
                blockedIndexes.add(translations.indexOf(word.getTranslation()));

                final Set<Word> answers = new HashSet<>();

                answers.add(word.getTranslation());


                for (int i = 0; i < 3; i++) {
                    int id = random.nextInt(translations.size());

                    if (!blockedIndexes.contains(id)) {
                        answers.add(translations.get(id));
                        blockedIndexes.add(id);
                    } else {
                        i--;
                    }
                }

                List<Word> unsortedAnswers = new ArrayList<>(answers);

                quests.add(new Quest(word.getWord(), unsortedAnswers, unsortedAnswers.indexOf(word.getTranslation())));
            }
        }

        return createSuccessContainer("Quests are formed", quests);
    }


    @Override
    @Nonnull
    protected Logger getLogger() {
        return LOGGER;
    }
}
