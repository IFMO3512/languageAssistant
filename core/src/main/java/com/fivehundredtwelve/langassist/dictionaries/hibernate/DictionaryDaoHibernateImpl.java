package com.fivehundredtwelve.langassist.dictionaries.hibernate;

import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.LanguageEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.WordEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author eliseev
 */
@Repository
@Transactional
public class DictionaryDaoHibernateImpl {
    @Autowired
    private SessionFactory sessionFactory;

    public void addWord(final String word, final String language) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setName(word);
        wordEntity.setLanguageEntity(registerLanguageEntity(language));

        final Session session = sessionFactory.getCurrentSession();
        session.save(wordEntity);
    }

//    @Nullable
//    public List<Word> getWords(final String word, final String language) {
//        final WordEntity wordEntity = getWordEntity(word, language);
//
//        if (wordEntity == null) return null;
//
//        return new Word(wordEntity.getName(), Language.getLanguage(language));
//    }

    private WordEntity getWordEntity(final String word, final String language) {
        Session currentSession = sessionFactory.getCurrentSession();

        return (WordEntity) currentSession.createQuery("from WordEntity where name = :name " +
                "and languageEntity = :languageEntity")
                .setParameter("name", word)
                .setParameter("languageEntity", getLanguageEntity(language))
                .uniqueResult();
    }

    private LanguageEntity getLanguageEntity(final String name) {
        Session currentSession = sessionFactory.getCurrentSession();

        return (LanguageEntity) currentSession.createQuery("from LanguageEntity where name = :name")
                .setParameter("name", name).uniqueResult();
    }

    private LanguageEntity registerLanguageEntity(final String name) {
        Session currentSession = sessionFactory.getCurrentSession();

        final LanguageEntity languageEntity = (LanguageEntity) currentSession.createQuery("from LanguageEntity where name = :name")
                .setParameter("name", name).uniqueResult();

        if (null == languageEntity) {
            final LanguageEntity newLanguageEntity = new LanguageEntity(name);
            currentSession.save(newLanguageEntity);

            return newLanguageEntity;
        }

        return languageEntity;
    }
}
