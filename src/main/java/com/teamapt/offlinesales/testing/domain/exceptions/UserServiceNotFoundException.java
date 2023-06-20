package com.teamapt.offlinesales.testing.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UserServiceNotFoundException extends ApiException{
    public UserServiceNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST.value());
    }

    public UserServiceNotFoundException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause, HttpStatus.BAD_REQUEST.value());
    }

    public UserServiceNotFoundException(String message, Object... args) {
        super(String.format(message, args), HttpStatus.BAD_REQUEST.value());

    }

    public UserServiceNotFoundException(Throwable cause) {
        super(cause,HttpStatus.BAD_REQUEST.value());
    }
}
