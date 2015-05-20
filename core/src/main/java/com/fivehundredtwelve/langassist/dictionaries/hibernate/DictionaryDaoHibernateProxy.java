package com.fivehundredtwelve.langassist.dictionaries.hibernate;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryDao;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.TranslationEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.WordEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author eliseev
 */
public class DictionaryDaoHibernateProxy implements DictionaryDao {
    @Autowired
    private DictionaryDaoHibernate dictionaryDaoHibernate;

    @Override
    public void addWord(final Word word) {
        dictionaryDaoHibernate.registerWord(word);
    }

    @Override
    public Set<Word> getWords() {
        final List<WordEntity> wordEntities = dictionaryDaoHibernate.getWordEntities();

        if (null == wordEntities) {
            return Collections.emptySet();
        } else {
            return wordEntities.stream().map(this::translateToWord).collect(Collectors.toSet());
        }
    }

    private Word translateToWord(final WordEntity wordEntity) {
        return new Word(wordEntity.getName(),
                Language.getLanguage(wordEntity.getLanguageEntity().getName()));
    }

    @Override
    public void addTranslation(final Word word, final Word translation) {
        final WordEntity sourceWordEntity = dictionaryDaoHibernate.registerWord(word);
        final WordEntity translationWordEntity = dictionaryDaoHibernate.registerWord(translation);

        dictionaryDaoHibernate.addTranslationEntity(sourceWordEntity, translationWordEntity);
    }

    @Override
    public Map<Word, List<Word>> getTranslations() {
        final List<TranslationEntity> translationEntities = dictionaryDaoHibernate.getTranslationEntities();

        if (null == translationEntities) {
            return Collections.emptyMap();
        }

        final HashMap<Word, List<Word>> wordListHashMap = new HashMap<>();

        for (final TranslationEntity translationEntity : translationEntities) {
            final Word source = translateToWord(translationEntity.getSource());

            final List<Word> translations = wordListHashMap.getOrDefault(source, new LinkedList<>());

            translations.add(translateToWord(translationEntity.getTranslation()));
        }

        return wordListHashMap;
    }

    @Override
    public Word getTranslation(final Word word, final Language language) {
        final List<TranslationEntity> translationEntities = dictionaryDaoHibernate
                .getTranslationEntities(dictionaryDaoHibernate.registerWord(word));

        if (null == translationEntities) {
            return null;
        }

        for (final TranslationEntity translationEntity : translationEntities) {
            if (translationEntity.getTranslation().getLanguageEntity().getName()
                    .equals(language.getLanguageEnglishName())) {
                return translateToWord(translationEntity.getTranslation());
            }
        }

        return null;
    }

    @Override
    public List<Word> getTranslations(final Word word) {
        final List<TranslationEntity> translationEntities = dictionaryDaoHibernate
                .getTranslationEntities(dictionaryDaoHibernate.registerWord(word));

        if (null == translationEntities) {
            return Collections.emptyList();
        }

        return translationEntities.stream().map(translationEntity -> translateToWord(translationEntity.getTranslation()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeWord(final Word word) {
        dictionaryDaoHibernate.removeWordEntity(dictionaryDaoHibernate.registerWord(word));
    }
}
