package com.example.movieReco.error;

public class NoResultException extends RuntimeException {

    public NoResultException(String message) {
        super(message);
    }

    public NoResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
