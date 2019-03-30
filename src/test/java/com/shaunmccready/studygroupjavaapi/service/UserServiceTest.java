package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.dto.UserDTO;
import com.shaunmccready.studygroupjavaapi.repository.UserDao;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private Auth0 auth0;

    @Autowired
    private UserDao userDao;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Get single user by id - success")
    void getUserByIdTest() {
        User user = createAndSaveUserAndSetupAuth0User();
        UserDTO userById = userService.getUserById(user.getId());

        Assertions.assertThat(userById).isNotNull();
        Assertions.assertThat(userById.getId()).isNotBlank();
    }

    @Test
    @DisplayName("Update user by id - success")
    void updateUserById() {
        User originalUser = createAndSaveUserAndSetupAuth0User();
        UserDTO updatedUserDto = new UserDTO()
                .setEmail("updatecheck@test.com")
                .setName("updated name")
                .setGivenName("New-given-name");

        UserDTO userToVerifyUpdate = userService.updateUserById(originalUser.getId(), updatedUserDto);
        Assertions.assertThat(userToVerifyUpdate.getId()).isEqualToIgnoringCase(originalUser.getId());
        Assertions.assertThat(userToVerifyUpdate.getEmail()).isEqualToIgnoringCase("updatecheck@test.com");
        Assertions.assertThat(userToVerifyUpdate.getEmailVerified()).isEqualTo(originalUser.getEmailVerified());
        Assertions.assertThat(userToVerifyUpdate.getName()).isEqualToIgnoringCase("updated name");
        Assertions.assertThat(userToVerifyUpdate.getGivenName()).isEqualToIgnoringCase("New-given-name");
        Assertions.assertThat(userToVerifyUpdate.getFamilyName()).isEqualToIgnoringCase(originalUser.getFamilyName());
        Assertions.assertThat(userToVerifyUpdate.getPicture()).isEqualToIgnoringCase(originalUser.getPicture());
        Assertions.assertThat(userToVerifyUpdate.getModified()).isAfterOrEqualsTo(originalUser.getModified());
    }
}
