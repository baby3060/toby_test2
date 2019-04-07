package com.tobsec.service.exception;

public class EmptyResultException extends RuntimeException {
    public EmptyResultException(String msg) {
        super(msg);
    }

    public EmptyResultException(String msg, Throwable e) {
        super(msg, e);
    }
}