package com.example.crud.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private Long serialVersionId=1L;
    public ResourceNotFoundException(String message){
        super(message);
    }
}
