package com.tgfc.library.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tgfc.library.enums.ErrorCodeException;
import com.tgfc.library.enums.ParamErrorCode;
import com.tgfc.library.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExeceptionAdvice {

    @ExceptionHandler
    @ResponseBody
    ResponseEntity handleException(Exception e) throws Exception {

        BaseResponse response = new BaseResponse();
        response.setStatus(false);
        if (e instanceof BindException) {
            response.setMessage(((BindException) e).getFieldError().getDefaultMessage());
            return ResponseEntity.status(400).body(response);
        } else if(e instanceof MethodArgumentTypeMismatchException){
            //路由綁定失敗
            String  fieldName=((MethodArgumentTypeMismatchException) e).getName();
            Object value = ((MethodArgumentTypeMismatchException) e).getValue();
            response.setMessage(fieldName + " : " + value);
            return ResponseEntity.status(400).body(response);
        } else if (e instanceof MethodArgumentNotValidException) {
            //hibernate @Validated 驗證失敗
            FieldError fieldError = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
            String fieldName = fieldError.getField();

            try {
                response.setMessage(fieldName);
                return ResponseEntity.status(403).body(response);
            } catch (IllegalArgumentException e1) {
                response.setMessage(fieldName);
                return ResponseEntity.status(400).body(response);
            }
        } else if (e instanceof ErrorCodeException) {
            //自定義驗證失敗
            response.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(response);
        } else if (e instanceof AccessDeniedException) {
            //@PreAuthorize 驗證失敗 直接丟出統一由SecurityExceptionHandler管理
            throw e;
        } else if(e instanceof HttpMessageNotReadableException) {
            //enum convert 錯誤
            if(e.getCause() instanceof InvalidFormatException){
                response.setMessage(((InvalidFormatException) e.getCause()).getPath().get(0).getFieldName());
                return ResponseEntity.status(400).body(response);
            }else{
                e.printStackTrace();
                response.setMessage(e.getMessage());
                return ResponseEntity.status(500).body(response);
            }
        }else{
            e.printStackTrace();
            response.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(response);
        }

    }
}
