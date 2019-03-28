package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.repository.GroupDao;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class GroupService {

    private Auth0 auth0;

    private GroupDao groupDao;

    private UserService userService;

    private UserGroupService userGroupService;

    public GroupService(Auth0 auth0, GroupDao groupDao, UserService userService, UserGroupService userGroupService) {
        this.auth0 = auth0;
        this.groupDao = groupDao;
        this.userService = userService;
        this.userGroupService = userGroupService;
    }


    public Group getGroupById(Long groupId) {
        Optional<Group> groupById = groupDao.findById(groupId);

        return groupById.orElseThrow(() -> new EntityNotFoundException("No group found with ID:" + groupId));
    }


    @Transactional
    public Group createGroup(Group group, User user) {
        Optional<Group> groupInDb = groupDao.findByName(group.getName());

        Group createdGroup = groupInDb.orElseGet(() -> createNewGroupInDatabase(group, user.getId()));

        userGroupService.addMemberToGroup(user, createdGroup);
        return createdGroup;
    }

    private Group createNewGroupInDatabase(Group group, String ownerId) {
        Assertions.assertThat(group.getName()).isNotBlank();
        Assertions.assertThat(ownerId).isNotBlank();
        Assertions.assertThat(group.getId()).isNull();

        group.setOwnerId(ownerId);
        group.setDefaults();

        return groupDao.save(group);
    }

}
