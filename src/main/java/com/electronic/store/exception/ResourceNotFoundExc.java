package com.electronic.store.exception;

public class ResourceNotFoundExc extends RuntimeException{
    public ResourceNotFoundExc() {
        super("Resource not found!!");
    }

    public ResourceNotFoundExc(String message) {
        super(message);
    }
}
