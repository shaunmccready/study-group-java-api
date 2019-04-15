package com.shaunmccready.studygroupjavaapi.controller;

import com.auth0.client.mgmt.ManagementAPI;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseControllerTest {

    @MockBean
    private ManagementAPI managementAPI;

    @MockBean
    private Auth0 auth0;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        when(auth0.stripBearer("Bearer test")).thenReturn("test");

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

}


