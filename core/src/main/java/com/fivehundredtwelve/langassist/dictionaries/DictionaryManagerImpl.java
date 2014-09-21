package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.Word;
import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author eliseev
 */
public class DictionaryManagerImpl implements DictionaryManager {
    private final static List<Word> NO_TRANSLATIONS = new ArrayList<>();

    private final Set<Word> words;

    private final Map<Word, List<Word>> translations;

    public DictionaryManagerImpl() {
        words = new CopyOnWriteArraySet<>();        // TODO compare with ConcurrentHashSet
        translations = new ConcurrentHashMap<>();
    }

    @Override
    public void addWord(@NotNull Word word) {
        Preconditions.checkNotNull(word, "Dictionary manager does not supports null element additions");

        words.add(word);
    }

    @Override
    @NotNull
    public Collection<Word> getWords() {
        return new HashSet<>(words);
    }

    @Override
    public void addTranslation(final @NotNull Word word, final @NotNull Word translation) {
        // TODO think about duplex translation
        Preconditions.checkNotNull(word, "Translated word shouldn't be null");
        Preconditions.checkNotNull(translation, "Translation shouldn't be null");

        words.add(word);
        words.add(translation);

        List<Word> newTranslations = translations.getOrDefault(word, new ArrayList<>());
        newTranslations.add(translation);

        translations.put(word, newTranslations);
    }

    @Override
    @Nullable
    public Word getTranslation(final @NotNull Word word, final @NotNull Languages language) {
        List<Word> _translations = translations.getOrDefault(word, NO_TRANSLATIONS);

        for (Word translation : _translations) {
            if (translation.getLanguage().equals(language)) {
                return translation;
            }
        }

        return null;
    }

    @Override
    @NotNull
    public Map<Word, List<Word>> getTranslations() {
        return new HashMap<>(translations);
    }


}
