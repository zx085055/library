package com.tgfc.library.controller;

import com.tgfc.library.entity.Schedule;
import com.tgfc.library.request.SchedulePageRequset;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.SchedulePageResponse;
import com.tgfc.library.service.IScheduleService;
import com.tgfc.library.service.imp.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
public class ScheduleController {

    @Autowired
    IScheduleService scheduleService;


    /**
     * TODO 新增排程
     * 傳入值:SchedulePageRequset (排程名稱，類型，通知時間，起始日期，結束日期，狀態)
     * 回傳值:Boolean
     */
    @PostMapping("/schedule/create")
    public BaseResponse create(@RequestBody SchedulePageRequset model) {
        BaseResponse response = new BaseResponse();
        response.setData(scheduleService.create(model));
        response.setMessage("新增成功");
        response.setStatus(true);
        return response;
    }


    /**
     * TODO 編輯排程
     * 傳入值:SchedulePageRequset (id，排程名稱，類型，通知時間，起始日期，結束日期，狀態)
     * 回傳值:Boolean
     */

    /**
     * 查詢排程列表
     * 傳入值:SchedulePageRequset (排程名稱，起始日期，結束日期，分頁頁數，分頁容量)
     * 回傳值:SchedulePageResponse (排程名稱，起始日期，結束日期，上次執行情況，狀態)
     */
    @PostMapping("/schedule/list")
    public BaseResponse list(@RequestBody SchedulePageRequset model) throws ParseException {
        BaseResponse response = new BaseResponse();
        response.setData(scheduleService.list(model));
        response.setMessage("查詢成功");
        response.setStatus(true);
        return response;
    }

    /**
     * TODO 刪除排程
     * 傳入值:排程ID
     * 回傳值:Boolean
     */
    @DeleteMapping("/schedule/delete")
    public BaseResponse delete(@RequestParam int id) {
        BaseResponse response = new BaseResponse();
        response.setData(scheduleService.delete(id));
        response.setMessage("刪除成功");
        response.setStatus(true);
        return response;
    }


    /**********測試用，不會留***********/


    @GetMapping("/schedule/list2")
    public Schedule list2() {
        return scheduleService.one();
    }

    @GetMapping("/schedule/list4")
    public Schedule list4() {
        return scheduleService.getone();
    }

    @GetMapping("/schedule/list3")
    public List<Schedule> list3() {
        return scheduleService.findAll();
    }


}
