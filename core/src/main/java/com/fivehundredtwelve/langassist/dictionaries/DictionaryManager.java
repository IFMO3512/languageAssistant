package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Word;

import java.util.Collection;

/**
 * Manages the {@link Word}s entities, stores them in database, gets them out of there
 *
 * @author eliseev
 */
public interface DictionaryManager {
    public void addWord(Word word);

    public Collection<Word> getWords();
}
