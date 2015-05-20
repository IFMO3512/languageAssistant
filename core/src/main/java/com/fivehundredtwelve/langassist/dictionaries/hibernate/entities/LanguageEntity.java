package com.fivehundredtwelve.langassist.dictionaries.hibernate.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author eliseev
 */ // TODO write anything!!!
@Entity
@Table(name = "LANGUAGES")
public class LanguageEntity implements Serializable {
    private static final long serialVersionUID = 3485452813728232828L;

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "NAME")
    private String name;

    public LanguageEntity(final String name) {
        this.name = name;
    }

    public LanguageEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }
}
