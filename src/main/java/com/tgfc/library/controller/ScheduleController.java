package com.tgfc.library.controller;

import com.tgfc.library.enums.PermissionEnum;
import com.tgfc.library.request.SchedulePageRequset;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.text.ParseException;

@RestController
public class ScheduleController {

    @Autowired
    IScheduleService scheduleService;


    /**
     * 新增排程
     * 傳入值:SchedulePageRequset (排程名稱，類型，通知時間，起始日期，結束日期，狀態)
     * 回傳值:Boolean
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping("/schedule/create")
    public BaseResponse create(@RequestBody SchedulePageRequset model) {
        return scheduleService.create(model);
    }

    /**
     * 編輯排程
     * 傳入值:SchedulePageRequset (id，排程名稱，類型，通知時間，起始日期，結束日期，狀態)
     * 回傳值:Boolean
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/schedule/edit")
    public BaseResponse edit(@RequestBody SchedulePageRequset model) {
        return scheduleService.edit(model);
    }

    /**
     * 查詢排程列表
     * 傳入值:SchedulePageRequset (排程名稱，起始日期，結束日期，分頁頁數，分頁容量)
     * 回傳值:SchedulePageResponse (排程名稱，起始日期，結束日期，上次執行情況，狀態)
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping("/schedule/list")
    public BaseResponse list(@RequestBody SchedulePageRequset model) throws ParseException {
        return scheduleService.list(model);
    }

    /**
     * 刪除指定排程
     * 傳入值:排程ID
     * 回傳值:Boolean
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @DeleteMapping("/schedule/delete")
    public BaseResponse delete(@RequestParam int id) {
        return scheduleService.delete(id);
    }

    /**
     * 改變排程狀態 ( 啟用 <---> 禁用 )
     * 傳入值:排程ID
     * 回傳:Boolean
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/schedule/changeStatus")
    public BaseResponse changeStatus(@RequestParam int id) {
        return scheduleService.changeStatus(id);
    }

    /**
     * 刪除全部排程
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @DeleteMapping("/schedule/deleteAllJobs")
    public BaseResponse deleteAllJobs() {
        return scheduleService.deleteAllJobs();
    }

    /**
     * 暫停全部排程
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/schedule/pauseAll")
    public BaseResponse pauseAll() {
        return scheduleService.pauseAll();
    }

    /**
     * 恢復全部被暫停排程
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/schedule/resumeAll")
    public BaseResponse resumeAll() {
        return scheduleService.resumeAll();
    }

}
