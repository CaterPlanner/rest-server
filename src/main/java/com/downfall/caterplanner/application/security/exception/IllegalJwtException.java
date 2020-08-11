package com.downfall.caterplanner.application.security.exception;

import org.springframework.security.core.AuthenticationException;

public class IllegalJwtException extends AuthenticationException {

    public IllegalJwtException(String msg) {
        super(msg);
    }
}
