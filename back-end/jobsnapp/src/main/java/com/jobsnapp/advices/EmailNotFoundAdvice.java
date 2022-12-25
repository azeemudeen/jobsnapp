package com.jobsnapp.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jobsnapp.exceptions.EmailNotFoundException;


@ControllerAdvice
public class EmailNotFoundAdvice  {

    @ResponseBody
    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(EmailNotFoundException ex) {
        return ex.getMessage();
    }
}