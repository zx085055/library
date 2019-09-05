package com.tgfc.library.advice;

import com.tgfc.library.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler
    @ResponseBody
    ResponseEntity handleException(Exception e) throws Exception {
        BaseResponse response = new BaseResponse();
        response.setStatus(false);
        e.printStackTrace();

        if (e instanceof AccessDeniedException){
            response.setMessage("權限不足,請檢查授權");
            return ResponseEntity.status(403).body(response);
        }else if (e instanceof MethodArgumentNotValidException){
            response.setMessage("參數錯誤,請檢查參數內容");
            return ResponseEntity.status(400).body(response);
        }
        response.setMessage(e.getLocalizedMessage());
        return ResponseEntity.status(500).body(response);
    }
}
