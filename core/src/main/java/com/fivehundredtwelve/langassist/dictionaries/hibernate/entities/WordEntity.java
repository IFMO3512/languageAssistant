package com.fivehundredtwelve.langassist.dictionaries.hibernate.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author eliseev
 */ // TODO write anything!!!
@Entity
@Table(name = "WORDS")
public class WordEntity implements Serializable {
    private static final long serialVersionUID = 2815861207581480110L;

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long id;

    @Column(name = "WORD")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LANGUAGE_ID")
    private LanguageEntity languageEntity;

    public WordEntity(final String name, final LanguageEntity languageEntity) {
        this.name = name;
        this.languageEntity = languageEntity;
    }

    public WordEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public LanguageEntity getLanguageEntity() {
        return languageEntity;
    }

    public void setLanguageEntity(final LanguageEntity languageEntity) {
        this.languageEntity = languageEntity;
    }
}
