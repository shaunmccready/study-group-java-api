package com.shaunmccready.studygroupjavaapi.controller;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import com.shaunmccready.studygroupjavaapi.service.GroupService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/group")
public class GroupController {

    private GroupService groupService;

    private Auth0 auth0;

    public GroupController(GroupService groupService, Auth0 auth0) {
        this.groupService = groupService;
        this.auth0 = auth0;
    }


    @PostMapping()
    public Group createGroup(@RequestBody Group group, HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        return groupService.createGroup(group, token);
    }

    @GetMapping(value = "{id}")
    public Group getGroup(@PathVariable("id") Long groupId) {
        return groupService.getGroupById(groupId);
    }

}
