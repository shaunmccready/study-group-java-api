package com.shaunmccready.studygroupjavaapi.service;


import com.auth0.client.mgmt.ManagementAPI;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.mock.EntityMockProvider;
import com.shaunmccready.studygroupjavaapi.repository.UserDao;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
abstract class BaseIntegrationTest {

    @MockBean
    private ManagementAPI managementAPI;

    @MockBean
    private Auth0 auth0;

    @Autowired
    private UserDao userDao;


    private User createAndSaveUser() {
        User user = EntityMockProvider.createUser();
        userDao.save(user);
        return user;
    }

    User createAndSaveUserAndSetupAuth0User() {
        User user = createAndSaveUser();
        setUpAuth0User(user.getId());
        return user;
    }

    /**
     * This is needed to create the user in the system based on the data returned from Auth0.
     * Must be called each time a new user is to be created in the DB
     *
     * @param userId The pre-generated user id
     */
    private void setUpAuth0User(String userId) {
        com.auth0.json.mgmt.users.User auth0User = EntityMockProvider.createAuth0User();
        auth0User.setId(userId);
        when(auth0.getAuth0UserFromToken(any(String.class))).thenReturn(auth0User);
    }
}
