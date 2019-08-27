package com.tgfc.library.service;

import com.tgfc.library.entity.Recommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRecommendService {
    Page<Recommend> select(String name, Pageable pageable);
    Boolean insert(Recommend recommend);
    Boolean update(Recommend recommend);
    Boolean delete(Integer id);
}
