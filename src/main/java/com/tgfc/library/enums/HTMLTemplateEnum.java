package com.tgfc.library.enums;


public enum HTMLTemplateEnum {
    RESERVATION_NEARLY_EXPIRED_EMAIL_TEMPLATE("reservationNearlyExpiredEmailTemplate", "預約即將到期通知模板"),
    RESERVATION_EXPIRED_EMAIL_TEMPLATE("reservationExpiredEmailTemplate", "預約到期通知模板"),
    LENDING_NEARLY_EXPIRED_EMAIL_TEMPLATE("LendingNearlyExpiredEmailTemplate", "借閱即將到期通知"),
    LENDING_EXPIRED_EMAIL_TEMPLATE("LendingExpiredEmailTemplate", "借閱到期通知"),
    ;

    private String code;
    private String trans;

    private HTMLTemplateEnum(String code, String trans) {
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

    public static String typeToCode(String type) {
        switch (type) {
            case "1":
                return RESERVATION_NEARLY_EXPIRED_EMAIL_TEMPLATE.getCode();
            case "2":
                return RESERVATION_EXPIRED_EMAIL_TEMPLATE.getCode();
            case "3":
                return LENDING_NEARLY_EXPIRED_EMAIL_TEMPLATE.getCode();
            case "4":
                return LENDING_EXPIRED_EMAIL_TEMPLATE.getCode();
            default:
        }
        return null;
    }
}

