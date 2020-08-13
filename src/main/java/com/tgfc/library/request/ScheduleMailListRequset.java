package com.tgfc.library.request;

import java.util.List;
import java.util.Map;

public class ScheduleMailListRequset {
    private List<Map<String, Object>> mailList;

    public List<Map<String, Object>> getMailList() {
        return mailList;
    }

    public void setMailList(List<Map<String, Object>> mailList) {
        this.mailList = mailList;
    }
}
