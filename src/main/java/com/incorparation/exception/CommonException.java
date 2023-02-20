package com.incorparation.exception;

public class CommonException extends RuntimeException{

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Exception ex) {
        super(message, ex);
    }
}
