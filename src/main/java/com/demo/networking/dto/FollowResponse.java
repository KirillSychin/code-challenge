package com.demo.networking.dto;

public class FollowResponse {
    private String username;
    private String follower;

    public FollowResponse(String username, String follower) {
        this.username = username;
        this.follower = follower;
    }

    public String getUsername() {
        return username;
    }

    public String getFollower() {
        return follower;
    }
}