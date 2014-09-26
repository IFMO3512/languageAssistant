package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.Word;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * Manages the {@link Word}s entities, stores them in database, gets them out of there
 *
 * @author eliseev
 */
public interface DictionaryManager {
    public void addWord(@NotNull Word word);

    @NotNull
    public Collection<Word> getWords();

    public void addTranslation(final @NotNull Word word, final @NotNull Word translation);

    @Nullable
    public Word getTranslation(final @NotNull Word word, final @NotNull Languages language);

    @NotNull
    public Map<Word, java.util.List<Word>> getTranslations();
}
