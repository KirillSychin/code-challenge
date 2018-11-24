package com.demo.networking.repository;

import com.demo.networking.model.Post;
import com.demo.networking.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @Before
    public void setUp() {
        user = givenUser();
    }

    @Test
    public void shouldCreatePost() {
        String text = RandomStringUtils.random(140);
        Post post = new Post(user, text);
        postRepository.save(post);
        entityManager.flush();

        List<Post> actualPosts = postRepository.findAllByAuthorOrderByCreationDateDesc(user);

        assertThat(actualPosts).hasSize(1);
        assertThat(actualPosts).extracting("text").contains(text);
    }

    @Test
    public void shouldThrowExceptionWhenTextExcess140Symbols() {
        Post post = new Post(user, RandomStringUtils.random(141));
        postRepository.save(post);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void shouldGetListOrderedPosts() {
        postRepository.save(new Post(user, "1"));
        postRepository.save(new Post(user, "2"));
        entityManager.flush();

        List<Post> actualPosts = postRepository.findAllByAuthorOrderByCreationDateDesc(user);
        assertThat(actualPosts.get(0).getText()).isEqualTo("2");
        assertThat(actualPosts.get(1).getText()).isEqualTo("1");
    }

    @Test
    public void shouldGetPostsFromFollowers() {
        User follower1 = givenUser();
        User follower2 = givenUser();
        user.getFollowers().add(follower1);
        user.getFollowers().add(follower2);

        userRepository.save(user);

        postRepository.save(new Post(follower1, "1"));
        postRepository.save(new Post(follower2, "2"));
        postRepository.save(new Post(follower1, "3"));
        entityManager.flush();

        List<Post> actualPosts = postRepository.findAllByAuthorsOrderByCreationDateDesc(user.getFollowers());
        assertThat(actualPosts.get(0).getText()).isEqualTo("3");
        assertThat(actualPosts.get(1).getText()).isEqualTo("2");
        assertThat(actualPosts.get(2).getText()).isEqualTo("1");
    }

    private User givenUser() {
        String username = UUID.randomUUID().toString();
        User user = new User(username);
        userRepository.save(user);
        return user;
    }
}