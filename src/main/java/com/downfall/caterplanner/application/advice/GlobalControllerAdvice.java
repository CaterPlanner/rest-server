package com.downfall.caterplanner.application.advice;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(HttpRequestException.class)
    public ResponseHeader<?> handleControllerRuntimeExcpetion(HttpRequestException e){
        e.printStackTrace();
        return ResponseHeader.builder()
                    .status(e.getErrorCode())
                    .message(e.getMessage())
                    .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseHeader<?> handleNotValidRequest(HttpMessageNotReadableException e){
        e.printStackTrace();
        return ResponseHeader.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("잘못된 데이터가 넘어왔습니다.")
                    .build();
    }

}
