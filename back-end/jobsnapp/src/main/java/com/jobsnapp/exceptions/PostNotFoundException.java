package com.jobsnapp.exceptions;
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String str) {
        super(str);
    }
}
