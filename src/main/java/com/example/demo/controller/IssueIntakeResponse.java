package com.example.demo.controller;

public class IssueIntakeResponse {
    private final boolean isSuccess;
    private final Errors[] errors;
    private final String referenceId;

    public IssueIntakeResponse(String referenceId) {//success
        this.isSuccess = true;
        this.errors = null;
        this.referenceId = referenceId;
    }

    public IssueIntakeResponse(String referenceId, Errors[] errors) {//failure
        this.isSuccess = false;
        this.errors = errors;
        this.referenceId = referenceId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Errors[] getErrors() {
        return errors;
    }

    public String getReferenceId() {
        return referenceId;
    }
}

