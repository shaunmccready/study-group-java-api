package com.shaunmccready.studygroupjavaapi.security;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Configuration
public class Auth0 {


    @Value("${auth0.client.id}")
    private String clientId;

    @Value("${auth0.client.secret}")
    private String clientSecret;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.audience}")
    private String apiAudience;


    /**
     * This requests a token from Auth0 in order to make API calls
     *
     * @return TokenHolder
     */
    @Bean
    public TokenHolder authTokenHolder() {
        AuthAPI authAPI = new AuthAPI(domain, clientId, clientSecret);
        AuthRequest authRequest = authAPI.requestToken("https://" + domain + "/api/v2/");

        TokenHolder holder = null;
        try {
            holder = authRequest.execute();
        } catch (Auth0Exception e) {
            e.printStackTrace();
        }

        return holder;
    }


    /**
     * This is the Management API for Auth0
     * Documentation: https://auth0.com/docs/api/management/v2
     *
     * @return ManagementAPI
     */
    @Bean
    public ManagementAPI managementAPI() {
        return new ManagementAPI(domain, authTokenHolder().getAccessToken());
    }


    public User getAuth0UserFromToken(String token) {
        DecodedJWT decode = JWT.decode(token);

        Request<User> userRequest = managementAPI().users().get(decode.getSubject(), null);
        User user;
        try {
            user = userRequest.execute();
        } catch (Auth0Exception e) {
            throw new EntityNotFoundException("Could not find a user in the system with the token provided. Check the info and try again");
        }

        return user;
    }

    public void testAuth0(String tokenWithBearer) {
        // DEBUG purposes only
        String token = stripBearer(tokenWithBearer);

        System.out.println("\n\n" + managementAPI().clients());

        UsersPage listUsers = null;
        try {
            listUsers = managementAPI().users().list(null).execute();
        } catch (Auth0Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n\nListing Users:");

        if (listUsers != null) {
            for (User user : listUsers.getItems()) {
                System.out.println(user.getEmail());
            }
        }

        System.out.println("\n\nCurrently logged in User Details:");

        User user = getAuth0UserFromToken(token);

        System.out.println(user.getEmail());
        System.out.println(user.getId());
        System.out.println(user.getPicture());
    }


    public String stripBearer(String token) {
        return token.replace("Bearer ", "");
    }

}
