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
import org.springframework.stereotype.Service;

@Service
public class Auth0 {


    @Value("${auth0.client.id}")
    private String clientId;

    @Value("${auth0.client.secret}")
    private String clientSecret;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.audience}")
    private String apiAudience;


    public void testAuth0(String tokenWithBearer) {
        // DEBUG purposes only

        String token = stripBearer(tokenWithBearer);

        AuthAPI authAPI = new AuthAPI(domain, clientId, clientSecret);
        AuthRequest authRequest = authAPI.requestToken("https://" + domain + "/api/v2/");
        TokenHolder holder = null;
        try {
            holder = authRequest.execute();
        } catch (Auth0Exception e) {
            e.printStackTrace();
        }
        ManagementAPI mgmt = new ManagementAPI(domain, holder.getAccessToken());
        System.out.println("\n\n" + mgmt.clients());

        UsersPage listUsers = null;
        try {
            listUsers = mgmt.users().list(null).execute();
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

        DecodedJWT decode = JWT.decode(token);

        Request<User> userRequest = mgmt.users().get(decode.getSubject(), null);
        User user = null;
        try {
            user = userRequest.execute();
        } catch (Auth0Exception e) {
            e.printStackTrace();
        }

        System.out.println(user.getEmail());
        System.out.println(user.getId());
        System.out.println(user.getPicture());


    }


    public String stripBearer(String token) {
        return token.replace("Bearer ", "");
    }
}
