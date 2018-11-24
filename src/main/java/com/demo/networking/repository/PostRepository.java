package com.demo.networking.repository;

import com.demo.networking.model.Post;
import com.demo.networking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthorOrderByCreationDateDesc(User author);

    @Query("from Post p where p.author IN (:authors) order by creationDate DESC")
    List<Post> findAllByAuthorsOrderByCreationDateDesc(Set<User> authors);
}
