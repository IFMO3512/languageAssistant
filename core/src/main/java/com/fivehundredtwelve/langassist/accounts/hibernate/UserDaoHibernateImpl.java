package com.fivehundredtwelve.langassist.accounts.hibernate;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.accounts.hibernate.entities.UserEntity;
import com.fivehundredtwelve.langassist.accounts.hibernate.entities.UserWordEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.LanguageEntity;
import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.WordEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author eliseev
 */
@Repository
@Transactional
public class UserDaoHibernateImpl {
    @Autowired
    private SessionFactory sessionFactory;

    public void addUser(final UserEntity userEntity) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(userEntity);
    }

    public void addUserWord(final String email, final String word, final String language) {
        final Session session = sessionFactory.getCurrentSession();
        UserWordEntity userWordEntity = new UserWordEntity();
        userWordEntity.setUserEntity(new UserEntity(email));
        WordEntity wordEntity = new WordEntity();
        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setName(language);
        wordEntity.setLanguageEntity(languageEntity);
        userWordEntity.setWordEntity(wordEntity);
        session.save(userWordEntity);
    }

    public List<UserEntity> listUsers() {
        return sessionFactory.getCurrentSession().createQuery("from UserEntity").list();
    }

    public User getUser(final String email) {
        Session currentSession = sessionFactory.getCurrentSession();
        UserEntity userEntity = (UserEntity) currentSession.createQuery("from UserEntity where email = :email")
                .setParameter("email", email).uniqueResult();

        if (userEntity != null) {
            return new User(userEntity.getEmail());
        } else {
            return null;
        }
    }
}
