package com.demo.networking.model;

import com.demo.networking.dto.ErrorField;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {
    private String message;
    private List<ErrorField> fields = new ArrayList<>();

    public ValidationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public List<ErrorField> getFields() {
        return fields;
    }

    public void addFieldError(String field, String message) {
        this.fields.add(new ErrorField(field, message));
    }
}