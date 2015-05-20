package com.fivehundredtwelve.langassist.dictionaries.hibernate;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.DictionaryDao;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.LanguageEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.TranslationEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.WordEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author eliseev
 */
@Repository
@Transactional
public class DictionaryDaoHibernateImpl implements DictionaryDao {      // TODO separate to two classes
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addWord(final Word word) {
        registerWordEntity(word.getWord(), getLanguageEntity(word.getLanguage().getLanguageEnglishName()));
    }

    @Override
    public Set<Word> getWords() {
        final List<WordEntity> wordEntities = getWordEntities();

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
        final WordEntity sourceWordEntity = getSourceWordEntity(word);
        final WordEntity translationWordEntity = getSourceWordEntity(translation);

        addTranslationEntity(sourceWordEntity, translationWordEntity);
    }

    @Override
    public Map<Word, List<Word>> getTranslations() {
        final List<TranslationEntity> translationEntities = getTranslationEntities();

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
        final List<TranslationEntity> translationEntities = getTranslationEntities(getSourceWordEntity(word));

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

    private WordEntity getSourceWordEntity(final Word word) {
        return registerWordEntity(word.getWord(), registerLanguageEntity(word.getLanguage().getLanguageEnglishName()));
    }

    @Override
    public List<Word> getTranslations(final Word word) {
        final List<TranslationEntity> translationEntities = getTranslationEntities(getSourceWordEntity(word));

        if (null == translationEntities) {
            return Collections.emptyList();
        }

        return translationEntities.stream().map(translationEntity -> translateToWord(translationEntity.getTranslation()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeWord(final Word word) {
        removeWordEntity(getSourceWordEntity(word));
    }

    private void addWord(final String word, final String language) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setName(word);
        wordEntity.setLanguageEntity(registerLanguageEntity(language));

        final Session session = sessionFactory.getCurrentSession();
        session.save(wordEntity);
    }

    private List<WordEntity> getWordEntities() {
        //noinspection unchecked
        return sessionFactory.getCurrentSession().createQuery("from WordEntity").list();
    }

    private WordEntity getWordEntity(final String word, final LanguageEntity language) {
        Session currentSession = sessionFactory.getCurrentSession();

        return (WordEntity) currentSession.createQuery("from WordEntity where name = :name " +
                "and languageEntity = :languageEntity")
                .setParameter("name", word)
                .setParameter("languageEntity", language)
                .uniqueResult();
    }

    private LanguageEntity getLanguageEntity(final String name) {
        Session currentSession = sessionFactory.getCurrentSession();

        return (LanguageEntity) currentSession.createQuery("from LanguageEntity where name = :name")
                .setParameter("name", name).uniqueResult();
    }

    private LanguageEntity registerLanguageEntity(final String name) {
        Session currentSession = sessionFactory.getCurrentSession();

        final LanguageEntity languageEntity = (LanguageEntity) currentSession
                .createQuery("from LanguageEntity where name = :name")
                .setParameter("name", name).uniqueResult();

        if (null == languageEntity) {
            final LanguageEntity newLanguageEntity = new LanguageEntity(name);
            currentSession.save(newLanguageEntity);

            return newLanguageEntity;
        }

        return languageEntity;
    }

    private void addTranslationEntity(final WordEntity source, final WordEntity translation) {
        sessionFactory.getCurrentSession().save(new TranslationEntity(source, translation));
    }

    private WordEntity registerWordEntity(final String name, final LanguageEntity languageEntity) {
        Session currentSession = sessionFactory.getCurrentSession();

        final WordEntity wordEntity = getWordEntity(name, languageEntity);

        if (null == wordEntity) {
            final WordEntity newWordEntity = new WordEntity(name, languageEntity);
            currentSession.save(newWordEntity);

            return newWordEntity;
        }

        return wordEntity;
    }

    private void removeWordEntity(final WordEntity wordEntity) {
        sessionFactory.getCurrentSession().delete(wordEntity);
    }

    private List<TranslationEntity> getTranslationEntities() {
        //noinspection unchecked
        return sessionFactory.getCurrentSession().createQuery("from TranslationEntity").list();
    }

    private List<TranslationEntity> getTranslationEntities(final WordEntity sourceWordEntity) {
        //noinspection unchecked
        return sessionFactory.getCurrentSession().createQuery("from TranslationEntity where source = :source")
                .setParameter("source", sourceWordEntity).list();
    }
}
