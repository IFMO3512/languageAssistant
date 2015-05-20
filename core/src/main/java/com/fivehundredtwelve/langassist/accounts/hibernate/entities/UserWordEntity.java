package com.fivehundredtwelve.langassist.accounts.hibernate.entities;

import com.fivehundredtwelve.langassist.dictionaries.hibernate.entities.WordEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author eliseev
 */ // TODO write anything!!!
@Entity
@Table(name = "USER_WORDS")
public class UserWordEntity implements Serializable {
    private static final long serialVersionUID = 7277554571719202428L;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    @Id
    private UserEntity userEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "WORD_ID")
    @Id
    private WordEntity wordEntity;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(final UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public WordEntity getWordEntity() {
        return wordEntity;
    }

    public void setWordEntity(final WordEntity wordEntity) {
        this.wordEntity = wordEntity;
    }
}
