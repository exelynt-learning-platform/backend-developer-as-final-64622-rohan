package com.rohan.booking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userShouldBeAbleToViewResources() throws Exception {

        String loginRequest = """
                {
                    "username": "user",
                    "password": "User@123"
                }
                """;

        String response = mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = com.jayway.jsonpath.JsonPath
                .read(response, "$.token");

        mockMvc.perform(
                        get("/resources")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void userShouldNotBeAbleToCreateResource() throws Exception {

        String loginRequest = """
            {
                "username": "user",
                "password": "User@123"
            }
            """;

        String response = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = com.jayway.jsonpath.JsonPath
                .read(response, "$.token");

        String resourceRequest = """
            {
                "name": "Test Room",
                "description": "Test resource",
                "location": "Pune",
                "price": 500.00,
                "available": true
            }
            """;

        mockMvc.perform(
                        post("/resources")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(resourceRequest)
                )
                .andExpect(status().isForbidden());
    }
}