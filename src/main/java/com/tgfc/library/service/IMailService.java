package com.tgfc.library.service;

import com.tgfc.library.response.MailResponse;

import java.util.List;
import java.util.Map;

public interface IMailService {

    List<MailResponse> getReservationExpiredList();


    List<MailResponse> getReservationNearlyExpiredList();


    List<MailResponse> getLendingNearlyExpiredList();


    List<MailResponse> getLendingExpiredJobList();


    Boolean batchMailing(List<Map<String, String>> map);


    Boolean batchTemplateMailing(List<MailResponse> list, String type);


    List<Map<String, String>> getMailDetail(List<MailResponse> list, String jobType);

}
