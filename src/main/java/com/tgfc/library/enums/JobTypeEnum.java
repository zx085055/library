package com.tgfc.library.enums;

import com.tgfc.library.schedule.job.*;

public enum JobTypeEnum {
    RESERVATION_NEARLY_EXPIRED("1", "預約到期狀態變更"),
    RESERVATION_EXPIRED("2", "預約到期"),
    LENDING_NEARLY_EXPIRED("3", "出借即將到期"),
    LENDING_EXPIRED("4", "出借到期"),
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

    public static String codeToTrans(String type) {
        switch (type) {
            case "1":
                return RESERVATION_NEARLY_EXPIRED.getTrans();
            case "2":
                return RESERVATION_EXPIRED.getTrans();
            case "3":
                return LENDING_NEARLY_EXPIRED.getTrans();
            case "4":
                return LENDING_EXPIRED.getTrans();
            default:
        }
        return null;
    }

    public static Class codeToClass(String type) {
        switch (type) {
            case "1":
                return ReservationNearlyExpiredJob.class;
            case "2":
                return ReservationExpiredJob.class;
            case "3":
                return LendingNearlyExpiredJob.class;
            case "4":
                return LendingExpiredJob.class;
            default:
        }
        return null;
    }
}

