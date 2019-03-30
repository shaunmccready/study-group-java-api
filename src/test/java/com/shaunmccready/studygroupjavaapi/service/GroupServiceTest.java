package com.shaunmccready.studygroupjavaapi.service;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.domain.UserGroup;
import com.shaunmccready.studygroupjavaapi.dto.GroupDTO;
import com.shaunmccready.studygroupjavaapi.mock.EntityMockProvider;
import com.shaunmccready.studygroupjavaapi.repository.GroupDao;
import com.shaunmccready.studygroupjavaapi.repository.UserDao;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class GroupServiceTest extends BaseIntegrationTest {


    @Autowired
    private GroupService groupService;

    @Autowired
    private Auth0 auth0;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserGroupService userGroupService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Posting a new group - success")
    @Rollback
    @Transactional
    public void createNewGroupTest() {
        User groupAdmin = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("createNewGroupTest");
        group.setOwnerId(groupAdmin.getId());

        GroupDTO createdGroup = groupService.createGroup(group, groupAdmin);
        Assertions.assertThat(createdGroup.getId()).isNotNull();
        Assertions.assertThat(createdGroup.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Testing the single group get - success")
    @Rollback
    @Transactional
    public void getGroupWithIdTest() {
        User groupAdmin = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("getGroupWithIdTest");
        group.setOwnerId(groupAdmin.getId());

        GroupDTO createdGroup = groupService.createGroup(group, groupAdmin);

        GroupDTO foundGroup = groupService.getGroupById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.getId());
    }


    @Test
    @DisplayName("Add create new group and add a member - success")
    @Rollback
    void addMemberToGroupTest() {
        // Create user(admin) of group
        User groupAdmin = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("addMemberToGroupTest");
        group.setOwnerId(groupAdmin.getId());

        GroupDTO createdGroup = groupService.createGroup(group, groupAdmin);

        Optional<Group> foundGroup = groupDao.findById(createdGroup.getId());

        if (foundGroup.isEmpty()){
            fail();
        }

        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.get().getId());

        Optional<UserGroup> existingUserGroup = userGroupService.findByMemberIdAndGroupId(groupAdmin.getId(), foundGroup.get().getId());

        if (existingUserGroup.isEmpty()){
            fail();
        }

        Assertions.assertThat(existingUserGroup.get().getId()).isNotNull();
        Assertions.assertThat(existingUserGroup.get().getApproved()).isTrue();

        //Add a second regular member to the group and not be approved
        User regularMember = EntityMockProvider.createUser();
        userDao.save(regularMember);

        UserGroup userGroup = userGroupService.addMemberToGroup(regularMember, foundGroup.get());
        Assertions.assertThat(userGroup.getId()).isNotNull();
        Assertions.assertThat(userGroup.getApproved()).isFalse();

        regularMember = userDao.findById(regularMember.getId()).get();

        Assertions.assertThat(regularMember.getUserGroups()
                .iterator()
                .next()
                .getId()).isEqualTo(userGroup.getId());

    }

    @Test
    @DisplayName("Get all groups for a user - success")
    @Rollback
    void getGroupsByUserIdTest(){
        // Create user(admin) of group
        User groupAdmin = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("Get Group test");
        group.setOwnerId(groupAdmin.getId());

        GroupDTO createdGroup = groupService.createGroup(group, groupAdmin);

        List<GroupDTO> groups = groupService.getGroupsByUserId(groupAdmin.getId());
        Assertions.assertThat(groups.size()).isGreaterThan(0);
        Assertions.assertThat(groups.get(0).getId()).isEqualTo(createdGroup.getId());
    }


    @Test
    @DisplayName("Join a group test - success")
    @Rollback
    void joinGroupTest(){
        // Create user(admin) of group
        User groupAdmin = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("joinGroupTest");
        group.setOwnerId(groupAdmin.getId());

        GroupDTO createdGroup = groupService.createGroup(group, groupAdmin);

        Optional<Group> foundGroup = groupDao.findById(createdGroup.getId());

        if (foundGroup.isEmpty()){
            fail();
        }

        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.get().getId());

        Optional<UserGroup> existingUserGroup = userGroupService.findByMemberIdAndGroupId(groupAdmin.getId(), foundGroup.get().getId());

        if (existingUserGroup.isEmpty()){
            fail();
        }

        Assertions.assertThat(existingUserGroup.get().getId()).isNotNull();
        Assertions.assertThat(existingUserGroup.get().getApproved()).isTrue();

        //Add a second regular member to the group
        User regularMember = EntityMockProvider.createUser();
        userDao.save(regularMember);

        GroupDTO joinedGroupDTO = groupService.joinGroup(regularMember, foundGroup.get().getId());
        Assertions.assertThat(joinedGroupDTO.getId()).isNotNull();
        Assertions.assertThat(joinedGroupDTO.getId()).isGreaterThan(0);

        regularMember = userDao.findById(regularMember.getId()).get();
        UserGroup verifyGroup = regularMember.getUserGroups()
                .iterator()
                .next();

        Assertions.assertThat(verifyGroup.getGroupId()).isEqualTo(joinedGroupDTO.getId());
        Assertions.assertThat(verifyGroup.getApproved()).isFalse();
    }

    @Test
    @DisplayName("Leave a group test - success")
    @Rollback
    void leaveGroupTest(){
        // Create user(admin) of group
        User groupAdmin = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("leaveGroupTest");
        group.setOwnerId(groupAdmin.getId());

        GroupDTO createdGroup = groupService.createGroup(group, groupAdmin);

        Optional<Group> foundGroup = groupDao.findById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.get().getId());

        Optional<UserGroup> existingUserGroup = userGroupService.findByMemberIdAndGroupId(groupAdmin.getId(), foundGroup.get().getId());
        Assertions.assertThat(existingUserGroup.isPresent()).isTrue();
        Assertions.assertThat(existingUserGroup.get().getId()).isNotNull();
        Assertions.assertThat(existingUserGroup.get().getApproved()).isTrue();

        //Add a second regular member to the group
        User regularMember = EntityMockProvider.createUser();
        userDao.save(regularMember);

        GroupDTO joinedGroupDTO = groupService.joinGroup(regularMember, foundGroup.get().getId());
        Assertions.assertThat(joinedGroupDTO.getId()).isNotNull();
        Assertions.assertThat(joinedGroupDTO.getId()).isGreaterThan(0);

        regularMember = userDao.findById(regularMember.getId()).get();
        UserGroup verifyGroup = regularMember.getUserGroups()
                .iterator()
                .next();

        Assertions.assertThat(verifyGroup.getGroupId()).isEqualTo(joinedGroupDTO.getId());
        Assertions.assertThat(verifyGroup.getApproved()).isFalse();

        groupService.leaveGroup(regularMember, joinedGroupDTO.getId());
        Optional<UserGroup> verifyLeftGroup = userGroupService.findByMemberIdAndGroupId(regularMember.getId(), joinedGroupDTO.getId());
        Assertions.assertThat(verifyLeftGroup).isEmpty();
    }


    @Test
    @DisplayName("Delete a group by Admin test - success")
    @Rollback
    void deleteGroupByAdminTest(){
        // Create user(admin) of group
        User groupAdmin = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("deleteGroupByAdminTest");
        group.setOwnerId(groupAdmin.getId());

        GroupDTO createdGroup = groupService.createGroup(group, groupAdmin);

        Optional<Group> foundGroup = groupDao.findById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.get().getId());

        Optional<UserGroup> existingUserGroup = userGroupService.findByMemberIdAndGroupId(groupAdmin.getId(), foundGroup.get().getId());
        Assertions.assertThat(existingUserGroup.isPresent()).isTrue();
        Assertions.assertThat(existingUserGroup.get().getId()).isNotNull();
        Assertions.assertThat(existingUserGroup.get().getApproved()).isTrue();

        //Add a second regular member to the group
        User regularMember = EntityMockProvider.createUser();
        userDao.save(regularMember);

        GroupDTO joinedGroupDTO = groupService.joinGroup(regularMember, foundGroup.get().getId());
        Assertions.assertThat(joinedGroupDTO.getId()).isNotNull();
        Assertions.assertThat(joinedGroupDTO.getId()).isGreaterThan(0);

        regularMember = userDao.findById(regularMember.getId()).get();
        UserGroup verifyGroup = regularMember.getUserGroups()
                .iterator()
                .next();

        Assertions.assertThat(verifyGroup.getGroupId()).isEqualTo(joinedGroupDTO.getId());
        Assertions.assertThat(verifyGroup.getApproved()).isFalse();

        groupService.deleteGroup(groupAdmin, joinedGroupDTO.getId());

        List<UserGroup> verifyDeleteGroupMembers = userGroupService.findByGroupId(createdGroup.getId());
        Assertions.assertThat(verifyDeleteGroupMembers).isEmpty();

        try {
            groupService.getGroupById(createdGroup.getId());
        } catch(EntityNotFoundException ignore){
            //success!
        }
    }


    @Test()
    @DisplayName("Delete a group by a non-admin test - Fail")
    @Rollback
    void deleteGroupByNonAdminTest(){
        // Create user(admin) of group
        User groupAdmin = createAndSaveUserAndSetupAuth0User();

        Group group = EntityMockProvider.createGroup("deleteGroupByNonAdminTest");
        group.setOwnerId(groupAdmin.getId());

        GroupDTO createdGroup = groupService.createGroup(group, groupAdmin);

        Optional<Group> foundGroup = groupDao.findById(createdGroup.getId());
        Assertions.assertThat(createdGroup.getId()).isEqualTo(foundGroup.get().getId());

        Optional<UserGroup> existingUserGroup = userGroupService.findByMemberIdAndGroupId(groupAdmin.getId(), foundGroup.get().getId());
        Assertions.assertThat(existingUserGroup.isPresent()).isTrue();
        Assertions.assertThat(existingUserGroup.get().getId()).isNotNull();
        Assertions.assertThat(existingUserGroup.get().getApproved()).isTrue();

        //Add a second regular member to the group
        User regularMember = EntityMockProvider.createUser();
        userDao.save(regularMember);

        GroupDTO joinedGroupDTO = groupService.joinGroup(regularMember, foundGroup.get().getId());
        Assertions.assertThat(joinedGroupDTO.getId()).isNotNull();
        Assertions.assertThat(joinedGroupDTO.getId()).isGreaterThan(0);

        final User regularMemberToDelete = userDao.findById(regularMember.getId()).get();
        UserGroup verifyGroup = regularMemberToDelete.getUserGroups()
                .iterator()
                .next();

        Assertions.assertThat(verifyGroup.getGroupId()).isEqualTo(joinedGroupDTO.getId());
        Assertions.assertThat(verifyGroup.getApproved()).isFalse();

        PersistenceException persistenceExceptionFromNonAdmin = assertThrows(PersistenceException.class, () -> groupService.deleteGroup(regularMemberToDelete,
                joinedGroupDTO.getId()),
                "Only Admins should be able to delete groups");
        Assertions.assertThat(persistenceExceptionFromNonAdmin.getMessage()).containsIgnoringCase("Unable to delete the group. Only the Admin/Owner of the group can do this");

        List<UserGroup> verifyDeleteGroupMembers = userGroupService.findByGroupId(createdGroup.getId());
        Assertions.assertThat(verifyDeleteGroupMembers.isEmpty()).isFalse();
    }
}
