package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.WordWithTranslation;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author eliseev
 */
public class DictionaryManagerImpl implements DictionaryManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryManagerImpl.class);

    private final Set<Word> words;

    private final Map<Word, List<Word>> translations;       // TODO recurrent words in translations =(

    public DictionaryManagerImpl() {
        words = new CopyOnWriteArraySet<>();        // TODO compare with ConcurrentHashSet
        translations = new ConcurrentHashMap<>();
    }

    @Override
    public void addWord(@Nonnull Word word) {
        Preconditions.checkNotNull(word, "Dictionary manager does not supports null element additions");

        words.add(word);
    }

    @Override
    @Nonnull
    public Collection<Word> getWords() {
        return new HashSet<>(words);
    }

    @Nonnull
    @Override
    public Collection<WordWithTranslation> getWordsWithTranslation(final @Nonnull Language language) {
        Preconditions.checkNotNull(language, "Language can't be null");

        LOGGER.debug("Getting all words with translation to language={}", language);

        Collection<WordWithTranslation> wordsWithTranslation = new ArrayList<>();

        for (final Word word : getWords()) {
            Word translation = getTranslation(word, language);

            if (translation == null) {
                wordsWithTranslation.add(new WordWithTranslation(word));
            } else {
                wordsWithTranslation.add(new WordWithTranslation(word,translation));
            }
        }

        LOGGER.debug("Returning words with translations");

        return wordsWithTranslation;
    }


    @Override
    public void addTranslation(final @Nonnull Word word, final @Nonnull Word translation) {
        Preconditions.checkNotNull(word, "Translated word shouldn't be null");
        Preconditions.checkNotNull(translation, "Translation shouldn't be null");

        LOGGER.debug("Adding word={} with translation={}", word, translation);

        words.add(word);
        words.add(translation);

        final List<Word> newTranslations = getTranslationList(word);
        newTranslations.add(translation);

        translations.put(word, newTranslations);
    }

    @Override
    @Nullable
    public Word getTranslation(final @Nonnull Word word, final @Nonnull Language language) {
        Preconditions.checkNotNull(word, "Translated word shouldn't be null");
        Preconditions.checkNotNull(language, "Language shouldn't be null");

        LOGGER.debug("Get translation for word={} and language={}", word, language);

        final List<Word> _translations = getTranslationList(word);

        LOGGER.debug("Found {} translations for word={}", _translations.size(), word);

        for (Word translation : _translations) {
            if (translation.getLanguage().equals(language)) {
                return translation;
            }
        }

        LOGGER.debug("Translation for word={} was not found", word);

        return null;
    }

    @Override
    @Nonnull
    public Map<Word, List<Word>> getTranslations() {
        LOGGER.debug("Getting translations");

        return new HashMap<>(translations);
    }

    @Override
    @Nonnull
    public List<Word> getTranslations(final @Nonnull Word word) {
        Preconditions.checkNotNull(word);

        LOGGER.debug("Getting translations for word={}", word);

        final List<Word> _translations = getTranslationList(word);

        LOGGER.debug("Found {} translations for word={}", _translations.size(), word);

        return new ArrayList<>(_translations);
    }

    @Override
    public void removeWord(final @Nonnull Word word) {
        Preconditions.checkNotNull(word);

        LOGGER.debug("Removing word={}", word);

        for (Word currentWord : words) {
            List<Word> _translations = getTranslationList(currentWord);
            if (_translations.remove(word)) {
                translations.put(currentWord, _translations);
            }
        }

        words.remove(word);
        translations.remove(word);
    }

    private List<Word> getTranslationList(final Word currentWord) {
        Preconditions.checkNotNull(currentWord);

        LOGGER.debug("Getting translation list for word={}", currentWord);

        final List<Word> _translations = translations.getOrDefault(currentWord, new ArrayList<>());

        LOGGER.debug("Found {} translations for word={}", _translations.size(), currentWord);

        return _translations;
    }

}
