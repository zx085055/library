package com.tgfc.library.controller;

import com.tgfc.library.entity.Recommend;
import com.tgfc.library.request.RecommendPageRequest;
import com.tgfc.library.service.IRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Autowired
    IRecommendService recommendService;

    @PostMapping("/select")
    public Page<Recommend> select(@RequestBody RecommendPageRequest recommend){
        return recommendService.select(recommend.getName(),recommend.getPageable());
    }

    @PostMapping("/insert")
    public Boolean insert(@RequestBody Recommend recommend){
        return recommendService.insert(recommend);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody Recommend recommend){
        return recommendService.update(recommend);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@Param("id") int id){
        return recommendService.delete(id);
    }



}
