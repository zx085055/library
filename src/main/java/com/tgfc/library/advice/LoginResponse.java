package com.tgfc.library.advice;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse extends CommonResponse{
    public LoginResponse(boolean success, Object data) {
        super(success,null, data);

    }

}
