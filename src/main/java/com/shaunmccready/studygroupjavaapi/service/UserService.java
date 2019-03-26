package com.shaunmccready.studygroupjavaapi.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.repository.UserDao;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserDao userDao;

    private Auth0 auth0;


    @Autowired
    public UserService(UserDao userDao, Auth0 auth0, ManagementAPI managementAPI) {
        this.userDao = userDao;
        this.auth0 = auth0;
    }


    public User getUser(String token) {
        com.auth0.json.mgmt.users.User userFromToken = auth0.getAuth0UserFromToken(token);
        Optional<User> userInDb = userDao.findById(userFromToken.getId());

        return userInDb.orElseGet(() -> createNewUserInDatabase(userFromToken));
    }


    private User createNewUserInDatabase(com.auth0.json.mgmt.users.User user) {
        User userToCreate = new User()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setEmailVerified(user.isEmailVerified())
                .setName(user.getName())
                .setGivenName(user.getGivenName())
                .setFamilyName(user.getFamilyName())
                .setPicture(user.getPicture())
                .setCreated(user.getCreatedAt())
                .setModified(user.getUpdatedAt());

        return userDao.save(userToCreate);
    }
}
