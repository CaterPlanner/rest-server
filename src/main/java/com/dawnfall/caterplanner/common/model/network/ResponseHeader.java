package com.dawnfall.caterplanner.common.model.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Getter
public final class ResponseHeader<T> extends ResponseEntity<T>{

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data<T> implements Serializable {
        private HttpStatus status;
        private String message;
        private T data;

    }


    private ResponseHeader(T body, HttpStatus status) {
        super(body, status);
    }

    public static <T> ResponseHeaderDataBuilder<T> builder() {
        return new ResponseHeaderDataBuilder<T>();
    }

    public static class ResponseHeaderDataBuilder<T> {
        private HttpStatus status;
        private String message;
        private T data;

        ResponseHeaderDataBuilder() {
        }

        public ResponseHeaderDataBuilder<T> status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public ResponseHeaderDataBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResponseHeaderDataBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseHeader<Data<T>> build() {
            Data<T> data = new Data<T>(this.status, this.message, this.data);
            return new ResponseHeader<Data<T>>(data, this.status);
        }

        public String toString() {
            return "ResponseHeader.ResponseHeaderBuilder(status=" + this.status + ", message=" + this.message + ", data=" + this.data + ")";
        }
    }


//    public static <T> ResponseHeaderBuilder<T> responseBuilder() {
//        return new ResponseHeaderBuilder<T>();
//    }
//
//    public static class ResponseHeaderBuilder<T> {
//        private HttpStatus status;
//        private String message;
//        private T data;
//
//        ResponseHeaderBuilder() {
//        }
//
//        public ResponseHeaderBuilder<T> status(HttpStatus status) {
//            this.status = status;
//            return this;
//        }
//
//        public ResponseHeaderBuilder<T> message(String message) {
//            this.message = message;
//            return this;
//        }
//
//        public ResponseHeaderBuilder<T> data(T data) {
//            this.data = data;
//            return this;
//        }
//
//        public ResponseEntity<ResponseHeader<T>> build() {
//            ResponseHeader<T> header = new ResponseHeader<T>(status, message, data);
//            return ResponseEntity.status(header.getStatus()).body(header);
//        }
//
//        public String toString() {
//            return "ResponseHeader.ResponseHeaderBuilder(status=" + this.status + ", message=" + this.message + ", data=" + this.data + ")";
//        }
//    }
}
