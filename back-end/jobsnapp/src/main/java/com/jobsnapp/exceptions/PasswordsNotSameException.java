package com.jobsnapp.exceptions;

public class PasswordsNotSameException extends RuntimeException {
    public PasswordsNotSameException() {
        super("Passwords are different");
    }
}
