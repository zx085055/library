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

    public static String code2Trans(String type){
        switch (type){
            case"1":
                return RESERVATION_EXPIRED.getTrans();
            case"2":
                return LENDING_NEARLY_EXPIRED.getTrans();
            case"3":
                return LENDING_EXPIRED.getTrans();
        }
        return null;
    }
}

