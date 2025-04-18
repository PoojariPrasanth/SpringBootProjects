package com.example.ecom_proj.exception;


import com.example.ecom_proj.repo.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    long serialVersionId=1L;
    public ResourceNotFoundException(String message){
        super(message);
    }
}
