package com.demo.cleanspringboot.exception;

public class DataNotFoundException extends RuntimeException {
    private String errorCode;

    public DataNotFoundException(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}