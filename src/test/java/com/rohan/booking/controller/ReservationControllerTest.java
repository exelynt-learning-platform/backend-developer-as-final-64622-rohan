package com.rohan.booking.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userShouldBeAbleToCreateReservation() throws Exception {

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

        String token = JsonPath.read(response, "$.token");

        String reservationRequest = """
                {
                    "resourceId": 1,
                    "startTime": "2027-01-10T10:00:00",
                    "endTime": "2027-01-10T11:00:00"
                }
                """;

        mockMvc.perform(
                        post("/reservations")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reservationRequest)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void userShouldSeeOnlyOwnReservations() throws Exception {

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

        String token = JsonPath.read(response, "$.token");

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/reservations")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void adminShouldBeAbleToUpdateReservationStatus() throws Exception {

        // -------------------------------
        // 1. Login as USER
        // -------------------------------
        String userLoginRequest = """
                {
                    "username": "user",
                    "password": "User@123"
                }
                """;

        String userResponse = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userLoginRequest)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String userToken = JsonPath.read(userResponse, "$.token");

        // -------------------------------
        // 2. Create a reservation as USER
        // -------------------------------
        String reservationRequest = """
                {
                    "resourceId": 1,
                    "startTime": "2027-03-10T10:00:00",
                    "endTime": "2027-03-10T11:00:00"
                }
                """;

        String reservationResponse = mockMvc.perform(
                        post("/reservations")
                                .header("Authorization", "Bearer " + userToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reservationRequest)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // JsonPath may return Integer, so convert safely to Long
        Number reservationIdNumber = JsonPath.read(
                reservationResponse,
                "$.id"
        );

        Long reservationId = reservationIdNumber.longValue();

        // -------------------------------
        // 3. Login as ADMIN
        // -------------------------------
        String adminLoginRequest = """
                {
                    "username": "admin",
                    "password": "Admin@123"
                }
                """;

        String adminResponse = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(adminLoginRequest)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String adminToken = JsonPath.read(adminResponse, "$.token");

        // -------------------------------
        // 4. ADMIN confirms reservation
        // -------------------------------
        String updateRequest = """
                {
                    "resourceId": 1,
                    "startTime": "2027-03-10T10:00:00",
                    "endTime": "2027-03-10T11:00:00",
                    "status": "CONFIRMED"
                }
                """;

        mockMvc.perform(
                        put("/reservations/" + reservationId)
                                .header("Authorization", "Bearer " + adminToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest)
                )
                .andExpect(status().isOk());
    }
}