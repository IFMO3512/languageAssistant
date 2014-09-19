package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Word;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eliseev
 */
public class DictionaryManagerImpl implements DictionaryManager {
    public Map<String, Word> words;

    public DictionaryManagerImpl() {
        words = new ConcurrentHashMap<>();
    }

    @Override
    public void addWord(Word word) {
        words.put(word.getWord(), word);
    }

    @Override
    public Collection<Word> getWords() {
        return words.values();
    }
}
