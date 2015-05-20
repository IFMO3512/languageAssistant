package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author eliseev
 */
public class DictionaryDaoCollections implements DictionaryDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryDaoCollections.class);

    private final Set<Word> words;

    private final Map<Word, List<Word>> translations;       // TODO recurrent words in translations =(

    public DictionaryDaoCollections() {
        words = new CopyOnWriteArraySet<>();                // TODO compare with ConcurrentHashSet
        translations = new ConcurrentHashMap<>();
    }

    @Override
    public void addWord(final Word word) {
        words.add(word);
    }

    @Override
    public Set<Word> getWords() {
        return new HashSet<>(words);
    }

    @Override
    public void addTranslation(final Word word, final Word translation) {
        words.add(word.minimal());
        words.add(translation.minimal());

        final List<Word> newTranslations = getTranslationList(word);
        newTranslations.add(translation);

        translations.put(word, newTranslations);

    }

    @Override
    public Map<Word, List<Word>> getTranslations() {
        return new HashMap<>(translations);
    }

    private List<Word> getTranslationList(final Word currentWord) {
        Preconditions.checkNotNull(currentWord);

        LOGGER.debug("Getting translation list for word={}", currentWord);

        final List<Word> _translations = translations.getOrDefault(currentWord, new ArrayList<>());

        LOGGER.debug("Found {} translations for word={}", _translations.size(), currentWord);

        return _translations;
    }

    @Override
    public Word getTranslation(final Word word, final Language language) {
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
    public List<Word> getTranslations(final Word word) {
        LOGGER.debug("Getting translations for word={}", word);

        final List<Word> _translations = getTranslationList(word);

        LOGGER.debug("Found {} translations for word={}", _translations.size(), word);

        return new ArrayList<>(_translations);
    }

    @Override
    public void removeWord(final Word word) {
        for (Word currentWord : words) {
            List<Word> _translations = getTranslationList(currentWord);
            if (_translations.remove(word)) {
                translations.put(currentWord, _translations);
            }
        }

        words.remove(word);
        translations.remove(word);
    }
}
