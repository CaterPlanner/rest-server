package com.dawnfall.caterplanner.application.advice;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.common.model.network.Response;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(HttpRequestException.class)
    public Response handleControllerRuntimeExcpetion(HttpRequestException e){
        e.printStackTrace();
        return new Response(e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response handleNotValidRequest(HttpMessageNotReadableException e){
        e.printStackTrace();
        return new Response("잘못된 데이터가 넘어왔습니다 ",e);
    }

}
