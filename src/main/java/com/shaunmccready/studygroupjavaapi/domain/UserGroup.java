package com.shaunmccready.studygroupjavaapi.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_group", schema = "public")
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    private Long groupId;

    private String memberId;

    private Boolean approved;

    private Date created;

    private Date modified;

    private User member;

    private Group group;

    public UserGroup setDefaults() {
        if (created == null) {
            created = new Date();
        }

        modified = new Date();

        return this;
    }

    @Id
    @SequenceGenerator(name = "user_group_id_seq", sequenceName = "public.user_group_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_group_id_seq")
    public Long getId() {
        return id;
    }

    public UserGroup setId(Long id) {
        this.id = id;
        return this;
    }

    @Column(name = "group_id")
    public Long getGroupId() {
        return groupId;
    }

    public UserGroup setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    @Column(name = "member_id")
    public String getMemberId() {
        return memberId;
    }

    public UserGroup setMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }

    public Boolean getApproved() {
        return approved;
    }

    public UserGroup setApproved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public UserGroup setCreated(Date created) {
        this.created = created;
        return this;
    }

    public Date getModified() {
        return modified;
    }

    public UserGroup setModified(Date modified) {
        this.modified = modified;
        return this;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, insertable = false, updatable = false)
    public User getMember() {
        return member;
    }

    public UserGroup setMember(User member) {
        this.member = member;
        return this;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
    public Group getGroup() {
        return group;
    }

    public UserGroup setGroup(Group group) {
        this.group = group;
        return this;
    }
}
