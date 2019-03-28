package com.shaunmccready.studygroupjavaapi.controller;

import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import com.shaunmccready.studygroupjavaapi.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private Auth0 auth0;

    private UserService userService;


    public UserController(Auth0 auth0, UserService userService) {
        this.auth0 = auth0;
        this.userService = userService;
    }


    @GetMapping
    public User getUser(HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        return userService.getUserFromToken(token);
    }
}

