package com.shaunmccready.studygroupjavaapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserDTO implements Serializable {

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


    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<GroupDTO> groups = new ArrayList<>(0);

    public String getId() {
        return id;
    }

    public UserDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public UserDTO setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public UserDTO setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public UserDTO setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public UserDTO setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public UserDTO setCreated(Date created) {
        this.created = created;
        return this;
    }

    public Date getModified() {
        return modified;
    }

    public UserDTO setModified(Date modified) {
        this.modified = modified;
        return this;
    }

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public UserDTO setGroups(List<GroupDTO> groups) {
        this.groups = groups;
        return this;
    }
}
