package com.shaunmccready.studygroupjavaapi.controller;

import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloTestSecurityController {

    @Autowired
    private Auth0 auth0;


    @GetMapping("/public")
    public String testNoAuthentication(HttpServletRequest request) {
        return "This is the public endpoint";
    }


    @GetMapping("/private")
    public String testAuthentication(HttpServletRequest request) {
        return "This is the private endpoint";
    }


    @GetMapping("/private-scoped")
    public String testAuthenticationScoped(HttpServletRequest request) {
        return "This is the private-scoped endpoint with read access";
    }


    @GetMapping("/testAuth0Calling")
    public String testAuth0Calling(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        auth0.testAuth0(token);
        return "testAuth0Calling finished";
    }


}
