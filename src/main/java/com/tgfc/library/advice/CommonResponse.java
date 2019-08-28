package com.tgfc.library.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tgfc.library.enums.IErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {

    public CommonResponse(){

    }

    public CommonResponse(IErrorCode errorCode, Object data) {
        this(false,errorCode,data);
    }

    public CommonResponse(boolean success, IErrorCode errorCode, Object data) {
        this.success = success;
        if(errorCode!=null){
            this.errorCode = errorCode.getCode();
        }
        this.data = data;
    }

    private boolean success;
    private String errorCode;
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
