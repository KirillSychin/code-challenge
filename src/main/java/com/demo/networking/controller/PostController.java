package com.demo.networking.controller;

import com.demo.networking.dto.PostRequest;
import com.demo.networking.dto.PostResponse;
import com.demo.networking.model.Post;
import com.demo.networking.model.User;
import com.demo.networking.service.PostService;
import com.demo.networking.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@Api(value = "/api/post", description = "Post controller", produces = "application/json", consumes = "application/json")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new post",
            notes = "Return the created object")
    public PostResponse createNewPost(@Valid @RequestBody PostRequest postRequest) {
        User author = userService.getOrCreateUser(postRequest.getUsername());
        Post post = postService.writePost(author, postRequest.getText());
        return new PostResponse(post);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get posts by username",
            notes = "Return list of posts were created by this user")
    public List<PostResponse> getUserPosts(@RequestParam("username") String username) {
        User author = userService.getOrCreateUser(username);
        List<Post> posts = postService.getPosts(author);
        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/trends", method = RequestMethod.GET)
    @ApiOperation(value = "Get posts created by followed users",
            notes = "Return list of posts were created by followed users")
    public List<PostResponse> getFollowersPosts(@RequestParam("username") String username) {
        User user = userService.getOrCreateUser(username);
        List<Post> posts = postService.getFollowersPosts(user);
        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }
}