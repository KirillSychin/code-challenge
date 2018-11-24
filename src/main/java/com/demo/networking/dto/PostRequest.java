package com.demo.networking.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

public class PostRequest {
    private String username;
    @ApiModelProperty(notes = "Text shouldn't have more than 140 characters")
    @NotEmpty
    @Length(max = 140)
    private String text;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
