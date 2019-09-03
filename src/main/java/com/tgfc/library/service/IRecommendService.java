package com.tgfc.library.service;

import com.tgfc.library.entity.Recommend;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRecommendService {
    BaseResponse select(String name, Pageable pageable);
    BaseResponse insert(Recommend recommend);
    BaseResponse update(Recommend recommend);
    BaseResponse delete(Integer id);
    BaseResponse findAll(Pageable pageable);
}
