package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.domain.UserGroup;
import com.shaunmccready.studygroupjavaapi.dto.GroupDTO;
import com.shaunmccready.studygroupjavaapi.mapper.GroupMapper;
import com.shaunmccready.studygroupjavaapi.repository.GroupDao;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private Auth0 auth0;

    private GroupDao groupDao;

    private UserService userService;

    private UserGroupService userGroupService;

    private final GroupMapper groupMapper;


    public GroupService(Auth0 auth0, GroupDao groupDao, UserService userService, UserGroupService userGroupService, GroupMapper groupMapper) {
        this.auth0 = auth0;
        this.groupDao = groupDao;
        this.userService = userService;
        this.userGroupService = userGroupService;
        this.groupMapper = groupMapper;
    }


    public GroupDTO getGroupById(Long groupId) {
        Optional<Group> groupById = groupDao.findById(groupId);

        Group group = groupById.orElseThrow(() -> new EntityNotFoundException("No group found with ID:" + groupId));
        return groupMapper.groupToDto(group);
    }


    @Transactional
    public GroupDTO createGroup(Group group, User user) {
        Optional<Group> groupInDb = groupDao.findByName(group.getName());

        Group createdGroup = groupInDb.orElseGet(() -> createNewGroupInDatabase(group, user.getId()));

        UserGroup userGroup = userGroupService.addMemberToGroup(user, createdGroup);

        if (userGroup != null && userGroup.getApproved() &&
                userGroup.getGroupId().equals(createdGroup.getId())) {
            return groupMapper.groupToDto(createdGroup);
        } else {
            throw new PersistenceException("Unable to join the group");
        }
    }


    private Group createNewGroupInDatabase(Group group, String ownerId) {
        Assertions.assertThat(group.getName()).isNotBlank();
        Assertions.assertThat(ownerId).isNotBlank();
        Assertions.assertThat(group.getId()).isNull();

        group.setOwnerId(ownerId);
        group.setDefaults();

        return groupDao.save(group);
    }


    public List<GroupDTO> getGroupsByUserId(String userId) {
        Assertions.assertThat(userId).isNotBlank();
        List<Group> groupsByUserId = groupDao.findGroupsByUserId(userId);
        return groupMapper.groupsToListOfDto(groupsByUserId);
    }


    public GroupDTO joinGroup(User user, Long groupId) {
        Optional<Group> group = groupDao.findById(groupId);


        UserGroup userGroup = userGroupService.addMemberToGroup(user,
                group.orElseThrow(() -> new EntityNotFoundException("Group with Id" + groupId + " does not exist in the system")));

        if (userGroup != null && userGroup.getGroupId().equals(group.get().getId())) {
            return groupMapper.groupToDto(group.get());
        } else {
            throw new PersistenceException("Unable to join the group");
        }
    }


    public GroupDTO leaveGroup(User user, Long groupId) {
        Optional<Group> group = groupDao.findById(groupId);

        if (group.isEmpty()) {
            throw new EntityNotFoundException("No group exists with id:" + groupId);
        }

        Boolean removed = userGroupService.removeUserFromGroup(user, group.get());
        if (removed) {
            return groupMapper.groupToDto(group.get());
        } else {
            throw new PersistenceException("Unable to leave the group");
        }
    }


}
