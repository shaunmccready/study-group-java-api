package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.domain.UserGroup;
import com.shaunmccready.studygroupjavaapi.mock.EntityMockProvider;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupServiceTest extends BaseIntegrationTest {


    @Autowired
    private GroupService groupService;

    @Autowired
    private Auth0 auth0;

    @Autowired
    private UserDao userDao;

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
        User user = EntityMockProvider.createUser();
        setUpAuth0User(user.getId());

        Group group = EntityMockProvider.createGroup("Create Group test");
        group.setOwnerId(user.getId());

        String token = "fake_token";

        Group createdGroup = groupService.createGroup(group, token);
        Assertions.assertThat(createdGroup.getId()).isNotNull();
        Assertions.assertThat(createdGroup.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Testing the single group get - success")
    @Rollback
    @Transactional
    public void getGroupWithIdTest() {
        User user = EntityMockProvider.createUser();
        setUpAuth0User(user.getId());

        Group group = EntityMockProvider.createGroup("Get Group test");
        group.setOwnerId(user.getId());

        String token = "fake_token";
        Group createdGroup = groupService.createGroup(group, token);

        Group foundGroup = groupService.getGroupById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.getId());
    }


    @Test
    @DisplayName("Add create new group and add a member - success")
    @Rollback
    void addMemberToGroupTest() {
        // Create user(admin) of group
        User user = EntityMockProvider.createUser();
        userDao.save(user);

        setUpAuth0User(user.getId());

        Group group = EntityMockProvider.createGroup("Get Group test");
        group.setOwnerId(user.getId());

        String token = "fake_token";
        Group createdGroup = groupService.createGroup(group, token);

        Group foundGroup = groupService.getGroupById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.getId());

        Optional<UserGroup> existingUserGroup = userGroupService.findByMemberAndGroup(user.getId(), group.getId());
        Assertions.assertThat(existingUserGroup.isEmpty()).isTrue();

        // Add the admin user as a member of the group and auto-approved
        UserGroup userGroup = userGroupService.addMemberToGroup(user, foundGroup);
        Assertions.assertThat(userGroup.getId()).isNotNull();
        Assertions.assertThat(userGroup.getApproved()).isTrue();


        //Add a second regular member to the group
        User regularMember = EntityMockProvider.createUser();
        userDao.save(regularMember);

        userGroup = userGroupService.addMemberToGroup(regularMember, foundGroup);
        Assertions.assertThat(userGroup.getId()).isNotNull();
        Assertions.assertThat(userGroup.getApproved()).isFalse();

        regularMember = userDao.findById(regularMember.getId()).get();

        Assertions.assertThat(regularMember.getGroups()
                .iterator()
                .next()
                .getId()).isEqualTo(userGroup.getId());

    }

    /**
     * This is needed to create the user in the system based on the data returned from Auth0.
     * Must be called each time a new user is to be created in the DB
     * @param userId The pre-generated user id
     */
    void setUpAuth0User(String userId) {
        com.auth0.json.mgmt.users.User auth0User = EntityMockProvider.createAuth0User();
        auth0User.setId(userId);
        when(auth0.getAuth0UserFromToken(any(String.class))).thenReturn(auth0User);
    }

}
