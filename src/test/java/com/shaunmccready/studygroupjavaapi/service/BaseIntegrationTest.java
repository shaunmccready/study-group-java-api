package com.shaunmccready.studygroupjavaapi.service;


import com.auth0.client.mgmt.ManagementAPI;
import com.shaunmccready.studygroupjavaapi.security.Auth0;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
abstract class BaseIntegrationTest {

    @MockBean
    private ManagementAPI managementAPI;

    @MockBean
    private Auth0 auth0;


}
