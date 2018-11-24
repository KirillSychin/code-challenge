package com.demo.networking.controller;

import com.demo.networking.model.Post;
import com.demo.networking.model.User;
import com.demo.networking.service.PostService;
import com.demo.networking.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {
    private static final String POST_TEXT = "a post text";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private PostService postService;
    @Mock
    private User user;
    @Mock
    private Post post;

    @Test
    public void shouldCreatePost() throws Exception {
        when(user.getUsername()).thenReturn("user");
        when(post.getId()).thenReturn(1L);
        when(post.getAuthor()).thenReturn(user);
        when(post.getText()).thenReturn(POST_TEXT);
        when(post.getCreationDate()).thenReturn(new Date());
        when(userService.getOrCreateUser(eq("user"))).thenReturn(user);
        when(postService.writePost(eq(user), eq(POST_TEXT))).thenReturn(post);

        mvc.perform(post("/api/post/")
                .contentType(APPLICATION_JSON)
                .content("{\"username\": \"user\", \"text\": \"a post text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("text", is(POST_TEXT)))
                .andExpect(jsonPath("username", is("user")))
                .andExpect(jsonPath("date", not(nullValue())));
    }

    @Test
    public void shouldGetErrorWhenPostTextMoreThan140() throws Exception {
        String text = RandomStringUtils.random(141);
        mvc.perform(post("/api/post/")
                .contentType(APPLICATION_JSON)
                .content("{\"username\": \"user\", \"text\": \"" + text + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Validation error")))
                .andExpect(jsonPath("fields[0].field", is("text")))
                .andExpect(jsonPath("fields[0].message", is("length must be between 0 and 140")));
    }

    @Test
    public void shouldGetUserPosts() throws Exception {
        when(user.getUsername()).thenReturn("user");
        when(post.getId()).thenReturn(1L);
        when(post.getAuthor()).thenReturn(user);
        when(post.getText()).thenReturn(POST_TEXT);
        when(post.getCreationDate()).thenReturn(new Date());
        when(userService.getOrCreateUser(eq("user"))).thenReturn(user);
        when(postService.getPosts(eq(user))).thenReturn(Collections.singletonList(post));

        mvc.perform(get("/api/post/?username=user")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is(POST_TEXT)))
                .andExpect(jsonPath("$[0].username", is("user")))
                .andExpect(jsonPath("$[0].date", not(nullValue())));
    }

    @Test
    public void shouldGetFollowersPosts() throws Exception {
        when(user.getUsername()).thenReturn("user");
        when(post.getId()).thenReturn(1L);
        when(post.getAuthor()).thenReturn(user);
        when(post.getText()).thenReturn(POST_TEXT);
        when(post.getCreationDate()).thenReturn(new Date());
        when(userService.getOrCreateUser(eq("user"))).thenReturn(user);
        when(postService.getFollowersPosts(eq(user))).thenReturn(Collections.singletonList(post));

        mvc.perform(get("/api/post/trends/?username=user")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is(POST_TEXT)))
                .andExpect(jsonPath("$[0].username", is("user")))
                .andExpect(jsonPath("$[0].date", not(nullValue())));
    }
}