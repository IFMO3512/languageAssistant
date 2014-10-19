package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
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
    public Word getTranslation(final @Nonnull Word word, final @Nonnull Language language);

    @Nonnull
    public Map<Word, List<Word>> getTranslations();

    @Nonnull
    public List<Word> getTranslations(final @Nonnull Word word);

    public void removeWord(final @Nonnull Word word);
}