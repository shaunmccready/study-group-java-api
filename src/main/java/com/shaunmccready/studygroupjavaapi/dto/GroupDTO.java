package com.shaunmccready.studygroupjavaapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class GroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    /**
     * Owner/Admin of the group
     */
    private String ownerId;

    private Date created;

    private Date modified;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<UserDTO> members = new ArrayList<>(0);

    public Long getId() {
        return id;
    }

    public GroupDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroupDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public GroupDTO setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public GroupDTO setCreated(Date created) {
        this.created = created;
        return this;
    }

    public Date getModified() {
        return modified;
    }

    public GroupDTO setModified(Date modified) {
        this.modified = modified;
        return this;
    }

    public List<UserDTO> getMembers() {
        return members;
    }

    public GroupDTO setMembers(List<UserDTO> members) {
        this.members = members;
        return this;
    }
}
