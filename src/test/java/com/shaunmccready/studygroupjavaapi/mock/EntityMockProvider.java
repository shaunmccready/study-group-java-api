package com.shaunmccready.studygroupjavaapi.mock;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import org.assertj.core.api.Assertions;

import java.util.Date;
import java.util.UUID;

public class EntityMockProvider {


    public static User createUser() {
        return new User()
                .setId(UUID.randomUUID().toString())
                .setEmail("fake@fake.com")
                .setEmailVerified(true)
                .setName("John Smith")
                .setGivenName("John")
                .setFamilyName("Smith")
                .setPicture("http://fakepicture.com/pic.png")
                .setCreated(new Date())
                .setModified(new Date());
    }

    public static Group createGroup(String name) {
        Assertions.assertThat(name).isNotBlank();

        return new Group()
                .setName(name)
                .setModified(new Date())
                .setCreated(new Date());

    }

    public static com.auth0.json.mgmt.users.User createAuth0User() {
        com.auth0.json.mgmt.users.User auth0User = new com.auth0.json.mgmt.users.User();
        auth0User.setId("12345abcde");
        auth0User.setEmail("fakeuser@hotmail.com");
        return auth0User;
    }


}
