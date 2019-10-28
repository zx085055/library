package com.tgfc.library.util;

import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseBuilder {

    private Page page = null;

    private String message = null;

    private boolean success = true;

    public ResponseBuilder content(Page page){
        this.page = page;
        return this;
    }

    public ResponseBuilder message(String message){
        this.message = message;
        return this;
    }

    public BaseResponse build(){
        BaseResponse baseResponse = new BaseResponse();
        if (page != null) {
        Map<String,Object> data = new HashMap<>();
            data.put("totalCount", page.getTotalElements());
            data.put("results", page.getContent());
            baseResponse.setData(data);
            baseResponse.setMessage(message);
            baseResponse.setStatus(success);
        }
        return baseResponse;
    }

}
