package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.Word;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * Manages the {@link Word}s entities, stores them in database, gets them out of there
 *
 * @author eliseev
 */
public interface DictionaryManager {
    public void addWord(@Nonnull Word word);

    @Nonnull
    public Collection<Word> getWords();

    public void addTranslation(final @Nonnull Word word, final @Nonnull Word translation);

    @Nullable
    public Word getTranslation(final @Nonnull Word word, final @Nonnull Languages language);

    @Nonnull
    public Map<Word, java.util.List<Word>> getTranslations();
}