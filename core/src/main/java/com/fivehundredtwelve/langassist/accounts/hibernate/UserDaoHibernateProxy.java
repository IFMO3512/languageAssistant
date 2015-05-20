package com.fivehundredtwelve.langassist.accounts.hibernate;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.accounts.AccountDao;
import com.fivehundredtwelve.langassist.accounts.hibernate.entities.UserEntity;
import com.fivehundredtwelve.langassist.accounts.hibernate.entities.UserWordEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.DictionaryDaoHibernate;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.WordEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author eliseev
 */
@Repository
@Transactional
public class UserDaoHibernateProxy implements AccountDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DictionaryDaoHibernate dictionaryDaoHibernate;

    @Override
    public void addUser(final User user) {
        if (getUser(user.getEmail()) == null) {
            sessionFactory.getCurrentSession().save(new UserEntity(user.getEmail()));
        }
    }

    @Override
    public User getUser(final String email) {
        UserEntity userEntity = getUserEntity(email);

        if (userEntity != null) {
            return new User(userEntity.getEmail());
        } else {
            return null;
        }
    }

    private UserEntity getUserEntity(final String email) {
        return (UserEntity) sessionFactory.getCurrentSession().createQuery("from UserEntity where email = :email")
                .setParameter("email", email).uniqueResult();
    }

    @Override
    public void addWordToUser(final User user, final Word word) {
        final UserEntity userEntity = getUserEntity(user.getEmail());
        final WordEntity wordEntity = dictionaryDaoHibernate.registerWord(word);

        sessionFactory.getCurrentSession().save(new UserWordEntity(userEntity, wordEntity));
    }

    @Override
    public List<Word> getWords(final User user) {
        @SuppressWarnings("unchecked")
        final List<UserWordEntity> userEntities = sessionFactory.getCurrentSession()
                .createQuery("from UserWordEntity where userEntity = :userEntity")
                .setParameter("userEntity", getUserEntity(user.getEmail())).list();

        return userEntities.stream().map(userWordEntity -> translateToWord(userWordEntity.getWordEntity()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeWord(final User user, final Word word) {
        sessionFactory.getCurrentSession().createQuery("delete UserWordEntity where userEntity = :userEntity " +
                "and wordEntity = :wordEntity")
                .setParameter("userEntity", getUserEntity(user.getEmail()))
                .setParameter("wordEntity", dictionaryDaoHibernate.registerWord(word)).executeUpdate();
    }

    private Word translateToWord(final WordEntity wordEntity) {
        return new Word(wordEntity.getName(),
                Language.getLanguage(wordEntity.getLanguageEntity().getName()));
    }
}
