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
        return groupMapper.groupToDto(getGroup(groupId));
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
        Group group = getGroup(groupId);


        UserGroup userGroup = userGroupService.addMemberToGroup(user, group);

        if (userGroup != null && userGroup.getGroupId().equals(group.getId())) {
            return groupMapper.groupToDto(group);
        } else {
            throw new PersistenceException("Unable to join the group");
        }
    }


    public GroupDTO leaveGroup(User user, Long groupId) {
        Group group = getGroup(groupId);

        Boolean removed = userGroupService.removeUserFromGroup(user, group);
        if (removed) {
            return groupMapper.groupToDto(group);
        } else {
            throw new PersistenceException("Unable to leave the group");
        }
    }


    public GroupDTO deleteGroup(User user, Long groupId) {
        Group group = getGroup(groupId);

        if (user.getId().equalsIgnoreCase(group.getOwnerId())) {
            groupDao.delete(group);
            userGroupService.deleteGroup(group.getId());
            return groupMapper.groupToDto(group);
        } else {
            throw new PersistenceException("Unable to delete the group. Only the Admin/Owner of the group can do this");
        }
    }


    private Group getGroup(Long groupId) {
        Optional<Group> group = groupDao.findById(groupId);

        if (group.isEmpty()) {
            throw new EntityNotFoundException("No group exists with id:" + groupId);
        }

        return group.get();
    }


    public GroupDTO changeOwnerOfGroupByAdmin(String currentOwnerId, String newOwnerId, Long groupId) {
        verifyOwnerOfGroup(currentOwnerId, groupId);
        return changeOwnerOfGroup(newOwnerId, groupId);
    }


    private void verifyOwnerOfGroup(String loggedInUserId, Long groupId) {
        Group group = getGroup(groupId);
        if (!group.getOwnerId().equalsIgnoreCase(loggedInUserId)) {
            throw new PersistenceException("Unable to change owner of the group. Only the Admin/Owner of the group can do this");
        }
    }


    private GroupDTO changeOwnerOfGroup(String newOwnerId, Long groupId) {
        Group group = getGroup(groupId);
        group.setOwnerId(newOwnerId);
        return groupMapper.groupToDto(groupDao.save(group));
    }
}
