package com.tobsec.service.sql.exception;

public class SqlRetriveFailException extends Exception {
    public SqlRetriveFailException(String msg) {
        super(msg);
    }

    public SqlRetriveFailException(String msg, Throwable throwAble) {
        super(msg, throwAble);
    }
}