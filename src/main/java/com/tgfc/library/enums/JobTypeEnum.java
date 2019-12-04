package com.tgfc.library.enums;


public enum JobTypeEnum {
    RESERVATION_NEARLY_EXPIRED("1", "預約即將到期通知", "ReservationNearlyExpiredJob"),
    RESERVATION_EXPIRED("2", "預約到期通知", "ReservationExpiredJob"),
    LENDING_NEARLY_EXPIRED("3", "借閱即將到期通知", "LendingNearlyExpiredJob"),
    LENDING_EXPIRED("4", "借閱到期通知", "LendingExpiredJob"),
    ;

    private String code;
    private String trans;
    private String name;

    JobTypeEnum(String code, String trans, String name) {
        this.code = code;
        this.trans = trans;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static String codeToName(String type) {
        switch (type) {
            case "1":
                return RESERVATION_NEARLY_EXPIRED.getName();
            case "2":
                return RESERVATION_EXPIRED.getName();
            case "3":
                return LENDING_NEARLY_EXPIRED.getName();
            case "4":
                return LENDING_EXPIRED.getName();
            default:
        }
        return null;
    }

}

