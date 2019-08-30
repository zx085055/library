package com.tgfc.library.enums;

public enum JobTypeEnum {
    RESERVATION_EXPIRED("1", "預約到期"),
    LENDING_NEARLY_EXPIRED("2","出借即將到期"),
    LENDING_EXPIRED("3","出借到期"),
    ;

    private String code;
    private String trans;
    private JobTypeEnum(String code, String trans) {
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

