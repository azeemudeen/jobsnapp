package com.jobsnapp.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Wrong password");
    }}
