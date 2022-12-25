package com.jobsnapp.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String str) {
        super(str);
    }
}
