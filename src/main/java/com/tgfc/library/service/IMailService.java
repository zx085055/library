package com.tgfc.library.service;

import com.tgfc.library.response.MailResponse;

import java.util.List;
import java.util.Map;

public interface IMailService {
    /**
     * 讀取預約即將過期名單
     */
    List<MailResponse> getReservationExpiredList();

    /**
     * 讀取預約過期名單
     */
    List<MailResponse> getReservationNearlyExpiredList();

    /**
     * 讀取借書即將過期名單
     */
    List<MailResponse> getLendingNearlyExpiredList();

    /**
     * 讀取借書過期名單
     */
    List<MailResponse> getLendingExpiredJobList();

    /**
     * 批量寄信
     */
    Boolean batchMailing(List<Map<String, String>> map);

    /**
     * 批量寄信(使用HTML模板)
     */
    Boolean batchTemplateMailing(List<MailResponse> list, String type);

    /**
     * 取得eamil信息
     */
    List<Map<String, String>> getMailDetail(List<MailResponse> list, String jobType);


}
