package com.tgfc.library.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class BaseResponse {

    private String message;

    private boolean status;

    private Object data;

    public BaseResponse(){

    }

    public BaseResponse(Builder builder){
        this.message = builder.message;
        this.status = builder.status;
        this.data = builder.data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }

    public static class Builder{
        private String message;

        private boolean status;

        private Page page;

        private Object data;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(boolean success) {
            this.status = success;
            return this;
        }

        public Builder content(Object data) {
            this.data = data;
            return this;
        }

        public Builder content(Page page) {
            this.page = page;
            return this;
        }

        public BaseResponse build() {
            if (page != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("totalCount", page.getTotalElements());
                map.put("results", page.getContent());
                this.data = map;
            }
            return new BaseResponse(this);
        }
    }
}
