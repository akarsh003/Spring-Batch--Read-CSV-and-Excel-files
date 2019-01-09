package com.example.demo.config;

public class Errors {
    private final String attributeName;
    private final String message;

    public Errors(String attributeName, String message) {
        this.attributeName = attributeName;
        this.message = message;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getMessage() {
        return message;
    }
}
