package com.tgfc.library.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class ScheduleController {

    /**
     * 測試用API
     * @return
     */
    @GetMapping(value = "/hellotest")
    public String get(){
        return "hello";
    }

    /**
     * TODO 新增排程(驗證)
     * 傳入值:無
     * 回傳值:字典檔 { 排程類型 ( 預約到期通知，出借即將到期通知，出借到期通知 )
     *              ，狀態 ( 啟用，禁用 )
     *              ，通知時間時與分的大小 }
     */

    /**
     * TODO 新增排程
     * 傳入值:SchedulePageRequset (排程名稱，類型，通知時間，起始日期，結束日期，狀態)
     * 回傳值:Boolean
     */

    /**
     * TODO 編輯排程(驗證)
     * 傳入值:排程ID (String)
     * 回傳值:字典檔 { 排程類型 ( 預約到期通知，出借即將到期通知，出借到期通知 )
     *              ，狀態 ( 啟用，禁用 )
     *              ，通知時間時與分的大小 }
     */

    /**
     * TODO 編輯排程
     * 傳入值:SchedulePageRequset (id，排程名稱，類型，通知時間，起始日期，結束日期，狀態)
     * 回傳值:Boolean
     */

    /**
     * TODO 查詢排程列表
     * 傳入值:SchedulePageRequset (排程名稱，起始日期，結束日期，分頁頁數，分頁容量)
     * 回傳值:SchedulePageResponse (序號，排程名稱，起始日期，結束日期，上次執行情況，狀態)
     */

    /**
     * TODO 刪除排程
     * 傳入值:排程ID
     * 回傳值:Boolean
     */



}
