package com.fivehundredtwelve.langassist.accounts.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author eliseev
 */ // TODO write anything!!
@Entity
@Table(name = "USERS")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 6483876928624550649L;

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long id;

    @Column(name = "EMAIL")
    private String email;

    public UserEntity(final String email) {
        this.email = email;
    }

    public UserEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String name) {
        this.email = name;
    }
}
