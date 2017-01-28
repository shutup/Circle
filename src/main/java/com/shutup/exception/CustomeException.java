package com.shutup.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by shutup on 2017/1/28.
 */
public class CustomeException extends RuntimeException {
    private boolean isSuccess;
    private HttpStatus httpStatus;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public CustomeException(String message, boolean isSuccess, HttpStatus httpStatus) {
        super(message);
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
    }

}
