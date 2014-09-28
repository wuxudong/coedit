package com.wupipi.coedit.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public class RestError {

    private final HttpStatus status;
    private final int code;
    private final String message;
    private final String developerMessage;
    @JsonIgnore
    private final Throwable throwable;

    public RestError(HttpStatus status, int code, String message, String developerMessage, Throwable throwable) {
        if (status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        }
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
        this.throwable = throwable;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}