package com.tgfc.library.controller;

import com.tgfc.library.entity.Recommend;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.request.RecommendPageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Autowired
    IRecommendService recommendService;

    @RolesAllowed({ "ROLE_FRONT", "ROLE_BACK" })
    @PostMapping("/select")
    public BaseResponse select(@RequestBody RecommendPageRequest recommend){
        return recommendService.select(recommend.getName(),recommend.getPageable());
    }

    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Recommend recommend){
        return recommendService.insert(recommend);
    }

    @PutMapping("/update")
    public BaseResponse update(@RequestBody Recommend recommend){
        return recommendService.update(recommend);
    }

    @DeleteMapping("/delete")
    public BaseResponse delete(@RequestParam int id){
        return recommendService.delete(id);
    }

    @PostMapping("/findAll")
    public BaseResponse findAll(@RequestBody PageableRequest pageableRequest){
        return  recommendService.findAll(pageableRequest.getPageable());
    }

}
