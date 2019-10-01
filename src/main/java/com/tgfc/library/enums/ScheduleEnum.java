package com.tgfc.library.enums;

public enum ScheduleEnum {
    JOB("Job", "Job名稱開頭"),
    GROUP("Group", "Group名稱開頭"),
    ;

    private String code;
    private String trans;

    ScheduleEnum(String code, String trans) {
        this.code = code;
        this.trans = trans;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }


}
