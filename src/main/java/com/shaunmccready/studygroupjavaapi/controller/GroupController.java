package com.shaunmccready.studygroupjavaapi.controller;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.dto.GroupDTO;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import com.shaunmccready.studygroupjavaapi.service.GroupService;
import com.shaunmccready.studygroupjavaapi.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/group")
public class GroupController {

    private UserService userService;

    private GroupService groupService;

    private Auth0 auth0;

    public GroupController(UserService userService, GroupService groupService, Auth0 auth0) {
        this.userService = userService;
        this.groupService = groupService;
        this.auth0 = auth0;
    }


    @PostMapping()
    public Group createGroup(@RequestBody Group group, HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        User user = userService.getUserFromToken(token);

        return groupService.createGroup(group, user);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GroupDTO getGroup(@PathVariable("id") Long groupId) {
        return groupService.getGroupById(groupId);
    }

}
