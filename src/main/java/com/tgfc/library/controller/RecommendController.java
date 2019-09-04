package com.tgfc.library.controller;

import com.tgfc.library.entity.Recommend;
import com.tgfc.library.enums.PermissionEnum;
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

    @RolesAllowed({PermissionEnum.Role.ADMIN,PermissionEnum.Role.USER})
    @PostMapping("/select")
    public BaseResponse select(@RequestBody RecommendPageRequest recommend){
        return recommendService.select(recommend.getName(),recommend.getPageable());
    }

    @RolesAllowed({ PermissionEnum.Role.USER})
    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Recommend recommend){
        return recommendService.insert(recommend);
    }

    @RolesAllowed({ PermissionEnum.Role.ADMIN})
    @PutMapping("/update")
    public BaseResponse update(@RequestBody Recommend recommend){
        return recommendService.update(recommend);
    }

    @RolesAllowed({  PermissionEnum.Role.ADMIN})
    @DeleteMapping("/delete")
    public BaseResponse delete(@RequestParam int id){
        return recommendService.delete(id);
    }

    @RolesAllowed({PermissionEnum.Role.ADMIN,PermissionEnum.Role.USER})
    @PostMapping("/findAll")
    public BaseResponse findAll(@RequestBody PageableRequest pageableRequest){
        return  recommendService.findAll(pageableRequest.getPageable());
    }

}
