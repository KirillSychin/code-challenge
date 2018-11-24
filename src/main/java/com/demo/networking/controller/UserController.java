package com.demo.networking.controller;

import com.demo.networking.dto.FollowRequest;
import com.demo.networking.dto.FollowResponse;
import com.demo.networking.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Api(value = "/api/user", description = "User controller", produces = "application/json", consumes = "application/json")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/follow", method = RequestMethod.PUT)
    @ApiOperation(value = "Follow a specified user",
            notes = "Return a result of the operation")
    public FollowResponse followUser(@RequestBody FollowRequest followRequest) {
        String currentUserName = followRequest.getUsername();
        String followerUserName = followRequest.getFollower();
        userService.followUsername(currentUserName, followerUserName);
        return new FollowResponse(currentUserName, followerUserName);
    }
}
