package com.teamapt.offlinesales.testing.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UserServiceException extends ApiException {

    public UserServiceException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public UserServiceException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public UserServiceException(String message, Object... args) {
        super(String.format(message, args), HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    public UserServiceException(Throwable cause) {
        super(cause, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
