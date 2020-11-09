package com.dawnfall.caterplanner.application.exception;

import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpRequestException extends RuntimeException {

    private ErrorInfo errorInfo;

    public HttpRequestException(String message, ErrorInfo errorInfo) {
        super(message);
        this.errorInfo = errorInfo;
    }


}
