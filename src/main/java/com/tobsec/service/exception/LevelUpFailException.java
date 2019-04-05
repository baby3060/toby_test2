package com.tobsec.service.exception;

public class LevelUpFailException extends RuntimeException {
    public LevelUpFailException(String msg) {
        super(msg);
    }

    public LevelUpFailException(String msg, Throwable t) {
        super(msg, t);
    }
}