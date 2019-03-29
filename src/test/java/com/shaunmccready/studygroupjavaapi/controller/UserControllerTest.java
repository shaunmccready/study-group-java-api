package com.shaunmccready.studygroupjavaapi.controller;

import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.dto.UserDTO;
import com.shaunmccready.studygroupjavaapi.mock.EntityMockProvider;
import com.shaunmccready.studygroupjavaapi.service.GroupService;
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

@WebMvcTest(controllers = UserController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void createUserTest() throws Exception {
        User userToPost = EntityMockProvider.createUser();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .header("Authorization", "Bearer " + "test"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    void getUserByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
                .header("Authorization", "Bearer " + "test"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void updateUserByIdTest() throws Exception {
        UserDTO userDTO = new UserDTO()
                .setName("Update controller test");

        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                .header("Authorization", "Bearer " + "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
