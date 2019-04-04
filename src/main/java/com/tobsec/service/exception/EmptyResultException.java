package com.tobsec.service.exception;

public class EmptyResultException extends RuntimeException {
    public EmptyResultException(String msg) {
        super(msg);
    }
}