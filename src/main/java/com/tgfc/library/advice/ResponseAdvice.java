package com.tgfc.library.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tgfc.library.enums.AuthenticationErrorCode;
import com.tgfc.library.enums.ErrorCodeException;
import com.tgfc.library.enums.ParamErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {

    public boolean supports(MethodParameter methodParameter, Class aClass) {

        return aClass.isAssignableFrom(MappingJackson2HttpMessageConverter.class);
    }

    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof CommonResponse) {
            return o;
        } else {
            return new CommonResponse(true, null, o);
        }

    }

    @ExceptionHandler
    @ResponseBody
    ResponseEntity handleException(Exception e) throws Exception {
        //TODO 針對Exception 回傳相對應 http code
        if (e instanceof BindException) {
            return ResponseEntity.status(400).body(new CommonResponse(false, ParamErrorCode.PARAMETER_ERROR, ((BindException) e).getFieldError().getDefaultMessage()));
        } else if(e instanceof MethodArgumentTypeMismatchException){
            //路由綁定失敗
            String  fieldName=((MethodArgumentTypeMismatchException) e).getName();
            Object value = ((MethodArgumentTypeMismatchException) e).getValue();
            return ResponseEntity.status(400).body(new CommonResponse(false, ParamErrorCode.PARAMETER_ERROR, fieldName + " : " + value));
        } else if (e instanceof MethodArgumentNotValidException) {
            //hibernate @Validated 驗證失敗
            FieldError fieldError = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
            String fieldName = fieldError.getField();
            String msg = fieldError.getDefaultMessage();
            try {
                AuthenticationErrorCode auth = AuthenticationErrorCode.valueOf(msg);
                return ResponseEntity.status(403).body(new CommonResponse(false, auth, fieldName));
            } catch (IllegalArgumentException e1) {
                return ResponseEntity.status(400).body(new CommonResponse(false, ParamErrorCode.PARAMETER_ERROR, fieldName + " : " + msg));
            }
        } else if (e instanceof ErrorCodeException) {
            //自定義驗證失敗
            return ResponseEntity.status(500).body(new CommonResponse(false, ((ErrorCodeException) e).getErrorCode(), e.getMessage()));
        } else if (e instanceof AccessDeniedException) {
            //@PreAuthorize 驗證失敗 直接丟出統一由SecurityExceptionHandler管理
            throw e;
        } else if(e instanceof HttpMessageNotReadableException) {
            //enum convert 錯誤
            if(e.getCause() instanceof InvalidFormatException){
                return ResponseEntity.status(400).body(new CommonResponse(false,ParamErrorCode.PARAMETER_ERROR,((InvalidFormatException) e.getCause()).getPath().get(0).getFieldName()));
            }else{
                e.printStackTrace();
                return ResponseEntity.status(500).body(new CommonResponse(false, null, e.getMessage()));
            }
        }else{
            e.printStackTrace();
            return ResponseEntity.status(500).body(new CommonResponse(false, null, e.getMessage()));
        }

    }
}
