package com.fivehundredtwelve.langassist.dictionaries.hibernate;

import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.LanguageEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.TranslationEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.WordEntity;

import java.util.List;

/**
 * @author eliseev
 */ // TODO write anything!!! 
public interface DictionaryDaoHibernate {
    void addWord(String word, String language);

    List<WordEntity> getWordEntities();

    WordEntity getWordEntity(String word, LanguageEntity language);

    LanguageEntity getLanguageEntity(String name);

    LanguageEntity registerLanguageEntity(String name);

    void addTranslationEntity(WordEntity source, WordEntity translation);

    WordEntity registerWordEntity(String name, LanguageEntity languageEntity);

    void removeWordEntity(WordEntity wordEntity);

    List<TranslationEntity> getTranslationEntities();

    List<TranslationEntity> getTranslationEntities(WordEntity sourceWordEntity);

    WordEntity registerWord(Word word);
}
