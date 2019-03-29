package com.shaunmccready.studygroupjavaapi.controller;

import com.shaunmccready.studygroupjavaapi.dto.UserDTO;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import com.shaunmccready.studygroupjavaapi.service.UserService;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping
    public UserDTO createUser(HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        return userService.createUserFromToken(token);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") String userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}")
    //TODO: modify the verifyLoggedInUser check to be done via PreAuthorize
    public UserDTO updateUserById(@PathVariable("id") String userId, @RequestBody UserDTO userDTO, HttpServletRequest request){
        userService.verifyLoggedInUserIsUpdatingSelf(userId, request.getHeader("Authorization"));
        return userService.updateUserById(userId, userDTO);
    }
}

