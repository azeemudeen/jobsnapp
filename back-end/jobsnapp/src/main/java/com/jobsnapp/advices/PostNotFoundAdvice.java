package com.jobsnapp.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jobsnapp.exceptions.PostNotFoundException;
import com.jobsnapp.exceptions.UserNotFoundException;

@ControllerAdvice
public class PostNotFoundAdvice  {

    @ResponseBody
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(PostNotFoundException ex) {
        return ex.getMessage();
    }
}