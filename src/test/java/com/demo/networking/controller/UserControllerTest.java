package com.demo.networking.controller;

import com.demo.networking.dto.FollowRequest;
import com.demo.networking.dto.FollowResponse;
import com.demo.networking.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;

    @Test
    public void shouldFollowUser() throws Exception {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setUsername("user");
        followRequest.setFollower("follower");
        FollowResponse followResponse = new FollowResponse("user", "follower");

        mvc.perform(put("/api/user/follow")
                .contentType(APPLICATION_JSON)
                .content("{\"username\": \"user\", \"follower\": \"follower\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", is(followResponse.getUsername())))
                .andExpect(jsonPath("follower", is(followResponse.getFollower())));
    }
}