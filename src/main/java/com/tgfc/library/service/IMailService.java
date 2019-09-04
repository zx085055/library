package com.tgfc.library.service;

import com.tgfc.library.entity.Records;
import com.tgfc.library.entity.Reservation;
import com.tgfc.library.response.MailResponse;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface IMailService {
    /**
     * 讀取預約過期名單
     */
    List<Reservation> getReservationExpiredList(Date startTime, Date endTime);

    /**
     * 讀取借書即將過期名單
     */
    List<Records> getLendingNearlyExpiredList(Date startTime,Date endTime);

    /**
     * 讀取借書過期名單
     */
    List<Records> getLendingExpiredJobList(Date startTime, Date endTime);

    /**
     * 批量寄信
     */
    Boolean batchMailing(List<Map<String,String>> map);


}
