package com.teamapt.offlinesales.testing.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UserServiceBadRequestException extends ApiException{
    public UserServiceBadRequestException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST.value());
    }

    public UserServiceBadRequestException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause, HttpStatus.BAD_REQUEST.value());
    }

    public UserServiceBadRequestException(String message, Object... args) {
        super(String.format(message, args), HttpStatus.BAD_REQUEST.value());

    }

    public UserServiceBadRequestException(Throwable cause) {
        super(cause,HttpStatus.BAD_REQUEST.value());
    }
}
