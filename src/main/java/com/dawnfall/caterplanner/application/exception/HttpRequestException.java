package com.dawnfall.caterplanner.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpRequestException extends RuntimeException {

    private HttpStatus errorCode;

    public HttpRequestException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
