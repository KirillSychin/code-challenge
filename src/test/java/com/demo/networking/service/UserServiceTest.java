package com.demo.networking.service;

import com.demo.networking.model.User;
import com.demo.networking.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private User user1;
    @Mock
    private User user2;
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void shouldFollowUserIfExists() {
        when(userRepository.findByUsername(eq("1"))).thenReturn(user1);
        when(userRepository.findByUsername(eq("2"))).thenReturn(user2);
        userService.followUsername("1", "2");

        verify(user1.getFollowers()).add(user2);
        verify(userRepository).save(user1);
    }

    @Test
    public void shouldCreateUserWhenDoesNotExist() {
        when(userRepository.findByUsername(eq("123"))).thenReturn(null);
        userService.getOrCreateUser("123");

        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getId()).isNull();
    }

    @Test
    public void shouldGetUserWhenExists() {
        when(user1.getId()).thenReturn(1L);
        when(userRepository.findByUsername(eq("123"))).thenReturn(user1);
        userService.getOrCreateUser("123");

        verify(userRepository).findByUsername("123");
    }

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserService();
        }
    }
}