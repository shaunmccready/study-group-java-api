package com.shaunmccready.studygroupjavaapi.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.dto.UserDTO;
import com.shaunmccready.studygroupjavaapi.mapper.UserMapper;
import com.shaunmccready.studygroupjavaapi.repository.UserDao;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import io.micrometer.core.instrument.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private UserDao userDao;

    private Auth0 auth0;

    private final UserMapper userMapper;


    @Autowired
    public UserService(UserDao userDao, Auth0 auth0, ManagementAPI managementAPI, UserMapper userMapper) {
        this.userDao = userDao;
        this.auth0 = auth0;
        this.userMapper = userMapper;
    }

    public UserDTO createUserFromToken(String token) {
        User userFromToken = getUserFromToken(token);

        return userMapper.userToDto(userFromToken);
    }

    public User getUserFromToken(String token) {
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

    public UserDTO getUserById(String userId) {
        Assertions.assertThat(userId).isNotBlank();

        Optional<User> userById = userDao.findById(userId);
        User user = userById.orElseThrow(() -> new EntityNotFoundException("No User found with ID:" + userId));
        return userMapper.userToDto(user);
    }

    public void verifyLoggedInUserIsUpdatingSelf(String userId, String token) {
        User userFromToken = getUserFromToken(token);
        Assertions.assertThat(userId).isEqualToIgnoringCase(userFromToken.getId());
    }

    public UserDTO updateUserById(String userId, UserDTO userDTO) {
        Assertions.assertThat(userId).isNotBlank();

        Optional<User> userById = userDao.findById(userId);
        User user = userById.orElseThrow(() -> new EntityNotFoundException("No User found with ID:" + userId));
        User updatedUser = updateUser(user, userDTO);
        return userMapper.userToDto(updatedUser);

    }

    private User updateUser(User user, UserDTO userDTO) {
        boolean modified = false;

        if (!StringUtils.isEmpty(userDTO.getEmail())) {
            user.setEmail(userDTO.getEmail());
            modified = true;
        }

        if (!StringUtils.isEmpty(userDTO.getName())) {
            user.setName(userDTO.getName());
            modified = true;
        }

        if (!StringUtils.isEmpty(userDTO.getGivenName())) {
            user.setGivenName(userDTO.getGivenName());
            modified = true;
        }

        if (!StringUtils.isEmpty(userDTO.getFamilyName())) {
            user.setFamilyName(userDTO.getFamilyName());
            modified = true;
        }

        if (!StringUtils.isEmpty(userDTO.getPicture())) {
            user.setPicture(userDTO.getPicture());
            modified = true;
        }

        if (modified) {
            user.setModified(new Date());
            return userDao.save(user);
        } else {
            return user;
        }
    }
}
