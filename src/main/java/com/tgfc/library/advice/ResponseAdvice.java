package com.tgfc.library.advice;

import com.tgfc.library.response.BaseResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {

    public boolean supports(MethodParameter methodParameter, Class aClass) {

        return aClass.isAssignableFrom(MappingJackson2HttpMessageConverter.class);
    }

    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof BaseResponse) {
            return o;
        } else {
            return new BaseResponse();
        }

    }


}
