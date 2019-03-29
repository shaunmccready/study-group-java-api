package com.shaunmccready.studygroupjavaapi.controller;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.mock.EntityMockProvider;
import com.shaunmccready.studygroupjavaapi.service.GroupService;
import com.shaunmccready.studygroupjavaapi.service.UserGroupService;
import com.shaunmccready.studygroupjavaapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(controllers = GroupController.class)
@ExtendWith(SpringExtension.class)
class GroupControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserGroupService userGroupService;


    @Test
    @WithMockUser
    void createGroupTest() throws Exception {
        Group groupToPost = EntityMockProvider.createGroup("Test Group");

        mockMvc.perform(MockMvcRequestBuilders.post("/group")
                .header("Authorization", "Bearer " + "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(groupToPost)))
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    void getGroupTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/1")
                .header("Authorization", "Bearer " + "test"))
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    @WithMockUser
    void joinGroupTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/group/1/join")
                .header("Authorization", "Bearer " + "test"))
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    void leaveGroupTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/group/1/leave")
                .header("Authorization", "Bearer " + "test"))
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
