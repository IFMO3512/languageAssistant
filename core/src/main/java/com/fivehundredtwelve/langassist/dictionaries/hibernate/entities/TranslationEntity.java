package com.fivehundredtwelve.langassist.dictionaries.hibernate.entities;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author eliseev
 */ // TODO write anything!!!
@Entity
@Table(name = "TRANSLATIONS")
public class TranslationEntity implements Serializable {
    private static final long serialVersionUID = 3117807803210225674L;
    @Id
    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "SOURCE_ID")
    private WordEntity source;

    @Id
    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "TRANSLATION_ID")
    private WordEntity translation;

    public TranslationEntity(final WordEntity source, final WordEntity translation) {
        this.source = source;
        this.translation = translation;
    }

    public TranslationEntity() {
    }

    public WordEntity getSource() {
        return source;
    }

    public void setSource(final WordEntity source) {
        this.source = source;
    }

    public WordEntity getTranslation() {
        return translation;
    }

    public void setTranslation(final WordEntity translation) {
        this.translation = translation;
    }
}
