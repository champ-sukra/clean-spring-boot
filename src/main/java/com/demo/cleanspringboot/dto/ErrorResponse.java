package com.demo.cleanspringboot.dto;

public class ErrorResponse {
    
    private String code;
    private String message;
    
    // Constructors
    public ErrorResponse() {}
    
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}