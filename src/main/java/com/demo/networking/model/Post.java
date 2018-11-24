package com.demo.networking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @NotNull
    private User author;
    @Size(max = 140)
    @NotNull
    private String text;
    @NotNull
    private Date creationDate;

    public Post() {
    }

    public Post(@NotNull User author, @Size(max = 140) String text) {
        this.author = author;
        this.text = text;
        this.creationDate = new Date();
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
