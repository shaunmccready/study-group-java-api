package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.domain.UserGroup;
import com.shaunmccready.studygroupjavaapi.dto.GroupDTO;
import com.shaunmccready.studygroupjavaapi.mock.EntityMockProvider;
import com.shaunmccready.studygroupjavaapi.repository.GroupDao;
import com.shaunmccready.studygroupjavaapi.repository.UserDao;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class GroupServiceTest extends BaseIntegrationTest {


    @Autowired
    private GroupService groupService;

    @Autowired
    private Auth0 auth0;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserGroupService userGroupService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Posting a new group - success")
    @Rollback
    @Transactional
    public void createNewGroupTest() {
        User user = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("Create Group test");
        group.setOwnerId(user.getId());

        String token = "fake_token";

        Group createdGroup = groupService.createGroup(group, user);
        Assertions.assertThat(createdGroup.getId()).isNotNull();
        Assertions.assertThat(createdGroup.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Testing the single group get - success")
    @Rollback
    @Transactional
    public void getGroupWithIdTest() {
        User user = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("Get Group test");
        group.setOwnerId(user.getId());

        Group createdGroup = groupService.createGroup(group, user);

        GroupDTO foundGroup = groupService.getGroupById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.getId());
    }


    @Test
    @DisplayName("Add create new group and add a member - success")
    @Rollback
    void addMemberToGroupTest() {
        // Create user(admin) of group
        User user = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("Get Group test");
        group.setOwnerId(user.getId());

        Group createdGroup = groupService.createGroup(group, user);

        Optional<Group> foundGroup = groupDao.findById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.get().getId());

        Optional<UserGroup> existingUserGroup = userGroupService.findByMemberAndGroup(user.getId(), group.getId());
        Assertions.assertThat(existingUserGroup.isPresent()).isTrue();
        Assertions.assertThat(existingUserGroup.get().getId()).isNotNull();
        Assertions.assertThat(existingUserGroup.get().getApproved()).isTrue();

        //Add a second regular member to the group
        User regularMember = EntityMockProvider.createUser();
        userDao.save(regularMember);

        UserGroup userGroup = userGroupService.addMemberToGroup(regularMember, foundGroup.get());
        Assertions.assertThat(userGroup.getId()).isNotNull();
        Assertions.assertThat(userGroup.getApproved()).isFalse();

        regularMember = userDao.findById(regularMember.getId()).get();

        Assertions.assertThat(regularMember.getUserGroups()
                .iterator()
                .next()
                .getId()).isEqualTo(userGroup.getId());

    }


}
