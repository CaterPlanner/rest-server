package com.downfall.caterplanner.common.model.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public final class ResponseHeader<T> {
    private HttpStatus status;
    private String message;
    private T data;

}
