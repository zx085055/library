package com.tgfc.library.enums;

public enum AuthenticationErrorCode implements IErrorCode {

    MUST_LOGIN("AUTH001"),
    PERMISSION_DENIED("AUTH002"),
    LOGIN_FAILED("AUTH003");

    private String code;

    AuthenticationErrorCode(String code){
        this.code=code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}

