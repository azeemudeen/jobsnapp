package com.jobsnapp.exceptions;

public class EmailExistsAlreadyException extends RuntimeException {
    public EmailExistsAlreadyException(String email) {
        super("Email "+ email +" exists already");
    }
}
