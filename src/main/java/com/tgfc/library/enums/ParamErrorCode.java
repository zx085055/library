package com.tgfc.library.enums;

public enum ParamErrorCode implements IErrorCode {

    PARAMETER_ERROR("PARAM001");

    private  String code;

    ParamErrorCode(String code){
        this.code=code;
    }
    @Override
    public String getCode() {
        return code;
    }
}
