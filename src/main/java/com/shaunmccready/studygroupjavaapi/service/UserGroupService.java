package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.domain.UserGroup;
import com.shaunmccready.studygroupjavaapi.repository.UserDao;
import com.shaunmccready.studygroupjavaapi.repository.UserGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserGroupService {

    private UserGroupDao userGroupDao;

    private UserDao userDao;

    public UserGroupService(UserGroupDao userGroupDao, UserDao userDao) {
        this.userGroupDao = userGroupDao;
        this.userDao = userDao;
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


    Optional<UserGroup> findByMemberIdAndGroupId(String memberId, Long groupId) {
        return userGroupDao.findByMemberIdAndGroupId(memberId, groupId);
    }


    List<UserGroup> findByGroupId(Long groupId) {
        return userGroupDao.findByGroupId(groupId);
    }


    /**
     * Helper method to remove a user from a group
     */
    Boolean removeUserFromGroup(User user, Group group) {
        boolean removed = false;

        if (!user.getId().equals(group.getOwnerId())
                && !user.getUserGroups().isEmpty()) {

            for (UserGroup userGroup : user.getUserGroups()) {
                if (userGroup.getGroupId().equals(group.getId())
                        && userGroup.getMemberId().equals(user.getId())) {
                    userGroupDao.delete(userGroup);
                    removed = true;
                    break;
                }
            }
        }

        return removed;
    }


    @Transactional
    public void deleteGroup(Long groupId) {
        userGroupDao.deleteAllWithGroupId(groupId);
    }
}
