package com.shaunmccready.studygroupjavaapi.domain;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "group", schema = "public")
@Slf4j
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    /**
     * Owner/Admin of the group
     */
    private String ownerId;

    private Date created;

    private Date modified;

    private List<User> members = new ArrayList<>(0);

    private Set<UserGroup> userGroups = new HashSet<>(0);

    public Group() {
    }

    public Group(Long id, String name, String ownerId, Date created, Date modified) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.created = created;
        this.modified = modified;
    }

    public Group setDefaults() {
        if (created == null) {
            created = new Date();
        }

        modified = new Date();

        return this;
    }

    @Id
    @SequenceGenerator(name = "group_id_seq", sequenceName = "public.group_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "group_id_seq")
    public Long getId() {
        return id;
    }

    public Group setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Group setName(String name) {
        this.name = name;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Group setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public Group setCreated(Date created) {
        this.created = created;
        return this;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getModified() {
        return modified;
    }

    public Group setModified(Date modified) {
        this.modified = modified;
        return this;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    public List<User> getMembers() {
        return members;
    }

    public Group setMembers(List<User> members) {
        this.members = members;
        return this;
    }

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }

    public Group setUserGroups(Set<UserGroup> userGroups) {
        this.userGroups = userGroups;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id.equals(group.id) &&
                name.equals(group.name) &&
                ownerId.equals(group.ownerId) &&
                created.equals(group.created) &&
                modified.equals(group.modified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ownerId, created, modified);
    }
}
