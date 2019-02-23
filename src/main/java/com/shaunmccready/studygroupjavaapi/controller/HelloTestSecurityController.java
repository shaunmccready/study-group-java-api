package com.shaunmccready.studygroupjavaapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloTestSecurityController {


    @GetMapping("/public")
    public String testNoAuthentication(){
        return "This is the public endpoint";
    }


    @GetMapping("/private")
    public String testAuthentication(){
        return "This is the private endpoint";
    }

    @GetMapping("/private-scoped")
    public String testAuthenticationScoped(){
        return "This is the private-scoped endpoint with read access";
    }


}
