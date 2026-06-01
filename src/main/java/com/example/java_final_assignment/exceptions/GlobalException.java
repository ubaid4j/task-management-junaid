package com.example.java_final_assignment.exceptions;

import lombok.Getter;


@Getter
public class GlobalException extends RuntimeException {

    private final int statusCode;

    public GlobalException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public GlobalException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
