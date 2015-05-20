package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author eliseev
 */ // TODO write anything!!! 
public interface DictionaryDao {
    void addWord(Word word);

    Set<Word> getWords();

    void addTranslation(Word word, Word translation);

    Map<Word, List<Word>> getTranslations();

    Word getTranslation(Word word, Language language);

    List<Word> getTranslations(Word word);

    void removeWord(Word word);
}
