package com.tgfc.library.service;

import com.tgfc.library.entity.Schedule;
import com.tgfc.library.request.SchedulePageRequset;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.SchedulePageResponse;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

public interface IScheduleService  {

    /**
     * 新增排程
     * 邏輯:根據排程類型選擇Job，預設每日執行一次通知，
     *      Job的name與group和Trigger的group相同，Trigger為每日x點x分執行
     *      讀取表得到需要通知的對象名單scheduleMailListRequset (收件人，收件人信箱，書名，到期日期)
     */
    BaseResponse create(SchedulePageRequset model);

    /**
     * 刪除排程
     * 邏輯:通過排程ID刪除該排程
     */
    BaseResponse delete(int id);

    /**
     * 編輯排程
     * 邏輯:通過排程ID編輯該排程
     */
    BaseResponse edit(SchedulePageRequset model);

    /**
     * 查詢排程
     */
    BaseResponse list(SchedulePageRequset model) throws ParseException;

    /**
     * TODO 批量寄信
     * 邏輯:根據傳入的scheduleMailListRequset (收件人，收件人信箱，書名，到期日期)
     *      呼叫MailUtil，達成批量寄信功能
     */

    /**
     * 改變排成狀態
     */
    BaseResponse changeStatus(int id);

    /**
     * 刪除全部排程
     */
    BaseResponse deleteAllJobs();

    /**
     * 暫停全部排程
     */
    BaseResponse pauseAll();

    /**
     * 恢復全部被暫停排程
     */
    BaseResponse resumeAll();



    /**********測試用，不會留***********/

    Schedule one();
    Schedule getone();
    List<Schedule> findAll();
    int xxx();

}
