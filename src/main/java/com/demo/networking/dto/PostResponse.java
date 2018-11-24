package com.demo.networking.dto;

import com.demo.networking.model.Post;

import java.util.Date;

public class PostResponse {
    private Long id;
    private Date date;
    private String username;
    private String text;

    public PostResponse(Post postEntity) {
        this.id = postEntity.getId();
        this.date = postEntity.getCreationDate();
        this.text = postEntity.getText();
        this.username = postEntity.getAuthor().getUsername();
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }
}