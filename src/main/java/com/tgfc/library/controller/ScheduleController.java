package com.tgfc.library.controller;

import com.tgfc.library.enums.PermissionEnum;
import com.tgfc.library.request.SchedulePageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IScheduleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.text.ParseException;

@RestController
public class ScheduleController {

    private final IScheduleService scheduleService;

    public ScheduleController(IScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 新增排程
     *
     * @param model (排程名稱，類型，通知時間，起始日期，結束日期，狀態)
     * @return BaseResponse
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping("/schedule/create")
    public BaseResponse create(@RequestBody SchedulePageRequest model) {
        return scheduleService.create(model);
    }

    /**
     * 編輯排程
     *
     * @param model (id，排程名稱，類型，通知時間，起始日期，結束日期，狀態)
     * @return BaseResponse
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/schedule/edit")
    public BaseResponse edit(@RequestBody SchedulePageRequest model) {
        return scheduleService.edit(model);
    }

    /**
     * 查詢排程列表
     *
     * @param model (排程名稱，起始日期，結束日期，分頁頁數，分頁容量)
     * @return BaseResponse
     * (排程名稱，起始日期，結束日期，上次執行情況，狀態)
     * @throws ParseException
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PostMapping("/schedule/list")
    public BaseResponse list(@RequestBody SchedulePageRequest model) throws ParseException {
        return scheduleService.list(model);
    }

    /**
     * 刪除指定排程
     *
     * @param id
     * @return BaseResponse
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @DeleteMapping("/schedule/delete")
    public BaseResponse delete(@RequestParam int id) {
        return scheduleService.delete(id);
    }

    /**
     * 變排程狀態 ( 啟用 <---> 禁用 )
     *
     * @param model
     * @return BaseResponse
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/schedule/changeStatus")
    public BaseResponse changeStatus(@RequestBody SchedulePageRequest model) {
        return scheduleService.changeStatus(model.getId());
    }

    /**
     * 刪除全部排程
     *
     * @return BaseResponse
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @DeleteMapping("/schedule/deleteAllJobs")
    public BaseResponse deleteAllJobs() {
        return scheduleService.deleteAllJobs();
    }

    /**
     * 暫停全部排程
     *
     * @return BaseResponse
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/schedule/pauseAll")
    public BaseResponse pauseAll() {
        return scheduleService.pauseAll();
    }

    /**
     * 恢復全部被暫停排程
     *
     * @return BaseResponse
     */
    @RolesAllowed({PermissionEnum.Role.ADMIN})
    @PutMapping("/schedule/resumeAll")
    public BaseResponse resumeAll() {
        return scheduleService.resumeAll();
    }

}
