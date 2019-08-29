package com.tgfc.library.advice;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse extends CommonResponse{
    public LoginResponse(boolean success, Object data) {
        super(success,null, data);
//        this.xsrfToken=xsrfToken;
    }
//    private String  xsrfToken;
//
//    public String getXsrfToken() {
//        return xsrfToken;
//    }
//
//    public void setXsrfToken(String xsrfToken) {
//        this.xsrfToken = xsrfToken;
//    }
}
