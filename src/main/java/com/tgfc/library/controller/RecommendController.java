package com.tgfc.library.controller;

import com.tgfc.library.entity.Recommend;
import com.tgfc.library.request.RecommendPageRequest;
import com.tgfc.library.service.IRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Autowired
    IRecommendService recommendService;

    @PostMapping("/select")
    public Page<Recommend> select(@RequestBody RecommendPageRequest recommend){
        return recommendService.select(recommend.getName(),recommend.getPageable());
    }



}
