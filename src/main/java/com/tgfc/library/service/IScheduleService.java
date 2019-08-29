package com.tgfc.library.service;

public interface IScheduleService {

    /**
     * TODO 讀取預約表，得到預約到期名單
     */

    /**
     * TODO 讀取借閱表，得到借閱即將到期名單
     */

    /**
     * TODO 讀取借閱表，得到借閱到期名單
     */

    /**
     * TODO 新增排程
     * 邏輯:根據排程類型選擇Job，預設每日執行一次通知，
     *      Job的name與group和Trigger的group相同，Trigger為每日x點(待確認)執行
     *      讀取表得到需要通知的對象名單scheduleMailListModel (收件人，收件人信箱，書名，到期日期)
     */

    /**
     * TODO 刪除排程
     * 邏輯:通過排程ID刪除該排程
     */

    /**
     * TODO 編輯排程
     * 邏輯:通過排程ID編輯該排程
     */

    /**
     * TODO 查詢排程
     * 邏輯:通過查詢條件回傳符合條件的排程
     */

    /**
     * TODO 批量寄信
     * 邏輯:根據傳入的scheduleMailListModel (收件人，收件人信箱，書名，到期日期)
     *      呼叫MailUtil，達成批量寄信功能
     */
}