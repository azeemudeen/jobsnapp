package com.jobsnapp.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jobsnapp.exceptions.EmailNotFoundException;
import com.jobsnapp.exceptions.ObjectExistsException;

@ControllerAdvice
public class ObjectExistsAdvice  {

    @ResponseBody
    @ExceptionHandler(ObjectExistsException.class)
    @ResponseStatus(HttpStatus.FOUND)
    String existsHandler(ObjectExistsException ex) {
        return ex.getMessage();
    }
}