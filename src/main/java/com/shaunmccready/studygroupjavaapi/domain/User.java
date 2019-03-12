package com.shaunmccready.studygroupjavaapi.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user", schema = "public")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String email;

    private Boolean emailVerified;

    private String name;

    private String givenName;

    private String familyName;

    private String picture;

    private Date created;

    private Date modified;

    @Id
    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public User setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public User setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public User setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public User setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public User setCreated(Date created) {
        this.created = created;
        return this;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getModified() {
        return modified;
    }

    public User setModified(Date modified) {
        this.modified = modified;
        return this;
    }
}
