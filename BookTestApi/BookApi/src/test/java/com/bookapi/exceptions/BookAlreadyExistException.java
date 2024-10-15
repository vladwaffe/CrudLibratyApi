package com.bookapi.exceptions;

public class BookAlreadyExistException extends RuntimeException {
    public BookAlreadyExistException(String message) {
        super(message);
    }
}

