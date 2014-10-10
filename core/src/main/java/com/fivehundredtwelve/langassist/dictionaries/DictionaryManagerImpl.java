package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author eliseev
 */
public class DictionaryManagerImpl implements DictionaryManager {
    private final Set<Word> words;

    private final Map<Word, List<Word>> translations;

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

    @Override
    public void addTranslation(final @Nonnull Word word, final @Nonnull Word translation) {
        // TODO think about duplex translation
        Preconditions.checkNotNull(word, "Translated word shouldn't be null");
        Preconditions.checkNotNull(translation, "Translation shouldn't be null");

        words.add(word);
        words.add(translation);

        List<Word> newTranslations = getTranslationList(word);
        newTranslations.add(translation);

        translations.put(word, newTranslations);
    }

    @Override
    @Nullable
    public Word getTranslation(final @Nonnull Word word, final @Nonnull Language language) {
        Preconditions.checkNotNull(word, "Translated word shouldn't be null");
        Preconditions.checkNotNull(language, "Language shouldn't be null");

        List<Word> _translations = getTranslationList(word);

        for (Word translation : _translations) {
            if (translation.getLanguage().equals(language)) {
                return translation;
            }
        }

        return null;
    }

    @Override
    @Nonnull
    public Map<Word, List<Word>> getTranslations() {
        return new HashMap<>(translations);
    }

    @Override
    @Nonnull
    public List<Word> getTranslations(final @Nonnull Word word) {
        Preconditions.checkNotNull(word);

        return new ArrayList<>(getTranslationList(word));
    }

    @Override
    public void removeWord(final @Nonnull Word word) {
        Preconditions.checkNotNull(word);

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
        return translations.getOrDefault(currentWord, new ArrayList<>());
    }

}
