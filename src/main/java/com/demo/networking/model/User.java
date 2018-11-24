package com.demo.networking.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<User> followers;
    @Column(unique = true)
    @NotNull
    private String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
        this.followers = new HashSet<>();
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
