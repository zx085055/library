package com.tgfc.library.enums;

public enum ScheduleStatus {
    ENABLE("1", "啟用"),
    DISABLE("2", "停用"),
    UNDONE("3","未成功建立"),
    ;

    private String code;
    private String trans;
    private ScheduleStatus(String code, String trans) {
        this.code = code;
        this.trans = trans;
    }

    public static String code2Trans(String type) {
        switch (type) {
            case "1":
                return ENABLE.getTrans();
            case "2":
                return DISABLE.getTrans();
            case "3":
                return UNDONE.getTrans();
            default:
        }
        return null;
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
