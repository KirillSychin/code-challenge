package com.demo.networking.service;

import com.demo.networking.model.Post;
import com.demo.networking.model.User;
import com.demo.networking.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Transactional
    public Post writePost(User user, String text) {
        Post post = new Post(user, text);
        return postRepository.save(post);
    }

    public List<Post> getPosts(User user) {
        return postRepository.findAllByAuthorOrderByCreationDateDesc(user);
    }

    public List<Post> getFollowersPosts(User user) {
        return postRepository.findAllByAuthorsOrderByCreationDateDesc(user.getFollowers());
    }
}