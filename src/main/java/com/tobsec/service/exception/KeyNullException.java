package com.tobsec.service.exception;

public class KeyNullException extends RuntimeException {
    public KeyNullException(String msg) {
        super(msg);
    }

    public KeyNullException(String msg, Throwable e) {
        super(msg, e);
    }
}