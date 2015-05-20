package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author eliseev
 */
public class DictionaryManagerImpl implements DictionaryManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryManagerImpl.class);

    @Autowired
    private DictionaryDao dictionaryDao;

    public DictionaryManagerImpl() {
    }

    @Override
    public void addWord(@Nonnull Word word) {
        Preconditions.checkNotNull(word, "Dictionary manager does not supports null element additions");

        dictionaryDao.addWord(word);
    }

    @Override
    @Nonnull
    public Collection<Word> getWords() {
        return dictionaryDao.getWords();
    }

    @Nonnull
    @Override
    public Collection<Word> getWordsWithTranslation(final @Nonnull Language language) {
        return getWordsWithTranslation(getWords(), language);
    }


    @Nonnull
    @Override
    public Collection<Word> getWordsWithTranslation(final @Nonnull Collection<Word> words,
                                                                   final @Nonnull Language language) {
        Preconditions.checkNotNull(language, "Language can't be null");

        LOGGER.debug("Getting all words with translation to language={}", language);

        Collection<Word> wordsWithTranslation = new ArrayList<>();

        for (final Word word : words) {
            Word translation = getTranslation(word, language);

            if (translation == null) {
                wordsWithTranslation.add(word);
            } else {
                wordsWithTranslation.add(word.withTranslation(translation));
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

        dictionaryDao.addTranslation(word, translation);
    }

    @Override
    @Nullable
    public Word getTranslation(final @Nonnull Word word, final @Nonnull Language language) {
        Preconditions.checkNotNull(word, "Translated word shouldn't be null");
        Preconditions.checkNotNull(language, "Language shouldn't be null");

        return dictionaryDao.getTranslation(word, language);
    }

    @Override
    @Nonnull
    public Map<Word, List<Word>> getTranslations() {
        LOGGER.debug("Getting translations");

        return dictionaryDao.getTranslations();
    }

    @Override
    @Nonnull
    public List<Word> getTranslations(final @Nonnull Word word) {
        Preconditions.checkNotNull(word);

        return dictionaryDao.getTranslations(word);
    }

    @Override
    public void removeWord(final @Nonnull Word word) {
        Preconditions.checkNotNull(word);

        LOGGER.debug("Removing word={}", word);

        dictionaryDao.removeWord(word);
    }
}
