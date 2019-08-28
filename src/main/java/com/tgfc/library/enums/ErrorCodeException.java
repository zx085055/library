package com.tgfc.library.enums;

public class ErrorCodeException extends Exception {

    private IErrorCode errorCode;
    private Object data;

    public ErrorCodeException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;

    }

    public ErrorCodeException(IErrorCode errorCode, String message, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;

    }

    public ErrorCodeException(IErrorCode errorCode, String message, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.data = data;

    }
    public IErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(IErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}