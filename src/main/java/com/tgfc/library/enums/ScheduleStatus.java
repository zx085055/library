package com.tgfc.library.enums;

public enum ScheduleStatus {
    ENABLE("1", "啟用"),
    DISABLE("2", "停用"),
    ;

    private String code;
    private String trans;
    private ScheduleStatus(String code, String trans) {
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
