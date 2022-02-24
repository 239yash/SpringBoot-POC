package com.poc.samplecrud.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String error) {
        super(error);
    }
}
