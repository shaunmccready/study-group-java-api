package com.shaunmccready.studygroupjavaapi.controller;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.dto.GroupDTO;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import com.shaunmccready.studygroupjavaapi.service.GroupService;
import com.shaunmccready.studygroupjavaapi.service.UserGroupService;
import com.shaunmccready.studygroupjavaapi.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/group")
public class GroupController {

    private UserService userService;

    private GroupService groupService;

    private UserGroupService userGroupService;

    private Auth0 auth0;

    public GroupController(UserService userService, GroupService groupService, UserGroupService userGroupService, Auth0 auth0) {
        this.userService = userService;
        this.groupService = groupService;
        this.userGroupService = userGroupService;
        this.auth0 = auth0;
    }


    /**
     * Creating a brand new group and setting the owner as the token User
     *
     * @param group   group info to post
     * @param request Authorized token user
     * @return GroupDTO
     */
    @PostMapping()
    public GroupDTO createGroup(@RequestBody Group group,
                                HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        User user = userService.getUserFromToken(token);

        return groupService.createGroup(group, user);
    }


    /**
     * Retrieving a group by id
     *
     * @param groupId group Id to get
     * @return GroupDTO
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GroupDTO getGroup(@PathVariable("id") Long groupId) {
        return groupService.getGroupById(groupId);
    }


    /**
     * Joining an existing group
     *
     * @param groupId group Id to join
     * @return GroupDTO
     */
    @PostMapping("/{id}/join")
    public GroupDTO joinGroup(@PathVariable("id") Long groupId,
                              HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        User user = userService.getUserFromToken(token);

        return groupService.joinGroup(user, groupId);
    }


    /**
     * Leave a group that you're already assigned to.
     * Owners cannot leave, they can only change ownership or delete
     *
     * @param groupId group Id to leave
     * @return GroupDTO
     */
    @PostMapping("/{id}/leave")
    public GroupDTO leaveGroup(@PathVariable("id") Long groupId,
                               HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        User user = userService.getUserFromToken(token);

        return groupService.leaveGroup(user, groupId);
    }


    /**
     * Delete a group by Admin and remove all members
     *
     * @param groupId group Id to delete
     * @return GroupDTO
     */
    @DeleteMapping("/{id}")
    public GroupDTO deleteGroup(@PathVariable("id") Long groupId,
                                HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        User user = userService.getUserFromToken(token);

        return groupService.deleteGroup(user, groupId);
    }


    /**
     * Change owner/admin of a group By the current admin
     *
     * @param groupId group to change
     * @param newOwnerId new owner id
     * @return GroupDTO
     */
    @PutMapping("/{groupId}/changeOwner/{newOwnerId}")
    public GroupDTO changeOwnerOfGroup(@PathVariable("groupId") Long groupId,
                                       @PathVariable("newOwnerId") String newOwnerId,
                                       HttpServletRequest request) {
        String token = auth0.stripBearer(request.getHeader("Authorization"));
        User loggedUser = userService.getUserFromToken(token);

        return groupService.changeOwnerOfGroupByAdmin(loggedUser.getId(), newOwnerId, groupId);
    }

}
