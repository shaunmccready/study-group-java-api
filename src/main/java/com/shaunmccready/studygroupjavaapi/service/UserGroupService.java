package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.domain.UserGroup;
import com.shaunmccready.studygroupjavaapi.repository.UserGroupDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserGroupService {

    private UserGroupDao userGroupDao;

    public UserGroupService(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }


    UserGroup addMemberToGroup(User member, Group group) {
        Optional<UserGroup> byMemberIdAndGroupId = userGroupDao.findByMemberIdAndGroupId(member.getId(), group.getId());

        return byMemberIdAndGroupId.orElseGet(() -> createUserGroup(member, group));


    }

    private UserGroup createUserGroup(User member, Group group) {
        Boolean approved = group.getOwnerId().equalsIgnoreCase(member.getId());

        UserGroup userGroup = new UserGroup();
        userGroup.setMember(member)
                .setMemberId(member.getId())
                .setGroup(group)
                .setGroupId(group.getId())
                .setApproved(approved)
                .setDefaults();

        return userGroupDao.save(userGroup);
    }


    Optional<UserGroup> findByMemberAndGroup(String memberId, Long groupId) {
        return userGroupDao.findByMemberIdAndGroupId(memberId, groupId);
    }
}
