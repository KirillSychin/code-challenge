package com.demo.networking.repository;

import com.demo.networking.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() {
        String username = UUID.randomUUID().toString();
        User user = new User(username);

        userRepository.save(user);
        entityManager.flush();

        User actualUser = userRepository.findByUsername(username);

        assertThat(actualUser.getUsername()).isEqualTo(username);
        assertThat(actualUser.getFollowers()).isEmpty();
    }

    @Test
    public void shouldCheckUsernameUnique() {
        User user = new User("user");

        userRepository.save(user);
        entityManager.flush();

        User user2 = new User("user");
        userRepository.save(user2);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    public void shouldThrowExceptionWhenCreateUserWithNullUsername() {
        User user = new User(null);

        userRepository.save(user);
        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void shouldAddFollower() {
        User follower = givenUser();

        User userWithFollowers = givenUser();
        userWithFollowers.getFollowers().add(follower);

        userRepository.save(userWithFollowers);
        entityManager.flush();

        User actualUser = userRepository.findByUsername(userWithFollowers.getUsername());
        assertThat(actualUser.getFollowers()).hasSize(1);
        assertThat(actualUser.getFollowers()).extracting("username").contains(follower.getUsername());
    }

    @Test
    public void shouldAddFollowerOnlyOnce() {
        User follower = givenUser();

        User userWithFollowers = givenUser();
        userWithFollowers.getFollowers().add(follower);
        userRepository.save(userWithFollowers);
        entityManager.flush();

        userWithFollowers.getFollowers().add(follower);
        userRepository.save(userWithFollowers);
        entityManager.flush();

        User actualUser = userRepository.findByUsername(userWithFollowers.getUsername());
        assertThat(actualUser.getFollowers()).hasSize(1);
        assertThat(actualUser.getFollowers()).extracting("username").contains(follower.getUsername());
    }

    private User givenUser() {
        String username = UUID.randomUUID().toString();
        return new User(username);
    }
}

