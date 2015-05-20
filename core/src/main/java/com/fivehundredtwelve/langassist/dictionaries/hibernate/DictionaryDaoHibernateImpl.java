package com.fivehundredtwelve.langassist.dictionaries.hibernate;

import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.LanguageEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.TranslationEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.WordEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class DictionaryDaoHibernateImpl implements DictionaryDaoHibernate {

    @Autowired
    SessionFactory sessionFactory;

    public DictionaryDaoHibernateImpl() {
    }

    @Override
    public void addWord(final String word, final String language) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setName(word);
        wordEntity.setLanguageEntity(registerLanguageEntity(language));

        final Session session = sessionFactory.getCurrentSession();
        session.save(wordEntity);
    }

    @Override
    public List<WordEntity> getWordEntities() {
        //noinspection unchecked
        return sessionFactory.getCurrentSession().createQuery("from WordEntity").list();
    }

    @Override
    public WordEntity getWordEntity(final String word, final LanguageEntity language) {
        Session currentSession = sessionFactory.getCurrentSession();

        return (WordEntity) currentSession.createQuery("from WordEntity where name = :name " +
                "and languageEntity = :languageEntity")
                .setParameter("name", word)
                .setParameter("languageEntity", language)
                .uniqueResult();
    }

    @Override
    public LanguageEntity getLanguageEntity(final String name) {
        Session currentSession = sessionFactory.getCurrentSession();

        return (LanguageEntity) currentSession.createQuery("from LanguageEntity where name = :name")
                .setParameter("name", name).uniqueResult();
    }

    @Override
    public LanguageEntity registerLanguageEntity(final String name) {
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

    @Override
    public void addTranslationEntity(final WordEntity source, final WordEntity translation) {
        sessionFactory.getCurrentSession().save(new TranslationEntity(source, translation));
    }

    @Override
    public WordEntity registerWordEntity(final String name, final LanguageEntity languageEntity) {
        Session currentSession = sessionFactory.getCurrentSession();

        final WordEntity wordEntity = getWordEntity(name, languageEntity);

        if (null == wordEntity) {
            final WordEntity newWordEntity = new WordEntity(name, languageEntity);
            currentSession.save(newWordEntity);

            return newWordEntity;
        }

        return wordEntity;
    }

    @Override
    public void removeWordEntity(final WordEntity wordEntity) {
        sessionFactory.getCurrentSession().delete(wordEntity);
    }

    @Override
    public List<TranslationEntity> getTranslationEntities() {
        //noinspection unchecked
        return sessionFactory.getCurrentSession().createQuery("from TranslationEntity").list();
    }

    @Override
    public List<TranslationEntity> getTranslationEntities(final WordEntity sourceWordEntity) {
        //noinspection unchecked
        return sessionFactory.getCurrentSession().createQuery("from TranslationEntity where source = :source")
                .setParameter("source", sourceWordEntity).list();
    }

    @Override
    public WordEntity registerWord(final Word word) {
        return registerWordEntity(word.getWord(), registerLanguageEntity(word.getLanguage().getLanguageEnglishName()));
    }
}