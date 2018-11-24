package com.demo.networking.service;

import com.demo.networking.model.Post;
import com.demo.networking.model.User;
import com.demo.networking.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PostServiceTest {
    private static final String TEXT = "text";
    @Autowired
    private PostService postService;
    @MockBean
    private PostRepository postRepository;
    @Mock
    private User user;
    @Mock
    private Post post;
    @Captor
    private ArgumentCaptor<Post> postCaptor;

    @Test
    public void shouldReturnPostsByAuthor() {
        List<Post> postList = singletonList(post);
        when(postRepository.findAllByAuthorOrderByCreationDateDesc(user)).thenReturn(postList);

        List<Post> posts = postService.getPosts(user);

        assertThat(posts).isEqualTo(postList);
    }

    @Test
    public void shouldWriteNewPost() {
        postService.writePost(user, TEXT);

        verify(postRepository).save(postCaptor.capture());

        assertThat(postCaptor.getValue().getText()).isEqualTo(TEXT);
    }

    @Test
    public void shouldGetFollowerPosts() {
        when(user.getFollowers()).thenReturn(singleton(user));
        when(postRepository.findAllByAuthorsOrderByCreationDateDesc(eq(user.getFollowers())))
                .thenReturn(singletonList(post));

        List<Post> posts = postService.getFollowersPosts(user);

        verify(postRepository).findAllByAuthorsOrderByCreationDateDesc(eq(user.getFollowers()));
        assertThat(posts).contains(post);
    }

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public PostService postService() {
            return new PostService();
        }
    }
}