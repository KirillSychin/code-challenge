package com.demo.networking.service;

import com.demo.networking.model.User;
import com.demo.networking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void followUsername(String currentUserName, String followerUserName) {
        User currentUser = getOrCreateUser(currentUserName);
        User followerUser = getOrCreateUser(followerUserName);
        currentUser.getFollowers().add(followerUser);

        userRepository.save(currentUser);
    }

    @Transactional
    public User getOrCreateUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.save(new User(username));
        }
        return user;
    }
}
