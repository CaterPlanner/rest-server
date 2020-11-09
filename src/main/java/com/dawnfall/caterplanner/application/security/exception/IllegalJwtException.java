package com.dawnfall.caterplanner.application.security.exception;

import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class IllegalJwtException extends AuthenticationException {

    private ErrorInfo errorInfo;

    public IllegalJwtException(ErrorInfo errorInfo) {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
    }
}
