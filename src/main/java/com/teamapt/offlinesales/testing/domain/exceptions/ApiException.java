package com.teamapt.offlinesales.testing.domain.exceptions;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int code;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable throwable, int code) {
        super(message, throwable);
        this.code = code;
    }
}