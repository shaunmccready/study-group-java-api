package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.mock.EntityMockProvider;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
public class GroupServiceTest extends BaseIntegrationTest {


    @Autowired
    private GroupService groupService;

    @Autowired
    private Auth0 auth0;

    @Autowired
    private UserService userService;


    @BeforeEach
    public void setUp() {
        com.auth0.json.mgmt.users.User auth0User = EntityMockProvider.createAuth0User();

        when(auth0.getAuth0UserFromToken(any(String.class))).thenReturn(auth0User);

        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Posting a new group - success")
    @Rollback
    public void createNewGroupTest() {
        User user = EntityMockProvider.createUser();
        Group group = EntityMockProvider.createGroup("Create Group test");
        group.setOwnerId(user.getId());

        String token = "fake_token";

        Group createdGroup = groupService.createGroup(group, token);
        Assertions.assertThat(createdGroup.getId()).isNotNull();
        Assertions.assertThat(createdGroup.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Testing the single group get")
    @Rollback
    public void getGroupWithIdTest() {
        User user = EntityMockProvider.createUser();
        Group group = EntityMockProvider.createGroup("Get Group test");
        group.setOwnerId(user.getId());

        String token = "fake_token";
        Group createdGroup = groupService.createGroup(group, token);

        Group foundGroup = groupService.getGroupById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.getId());
    }


}
