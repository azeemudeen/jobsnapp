package com.jobsnapp.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jobsnapp.exceptions.UserNotFoundException;
import com.jobsnapp.exceptions.WrongPasswordException;


@ControllerAdvice
public class WrongPasswordAdvice  {

    @ResponseBody
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String wrongPasswordHandler(WrongPasswordException ex) {
        return ex.getMessage();
    }
}