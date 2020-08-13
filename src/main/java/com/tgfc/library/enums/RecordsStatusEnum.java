package com.tgfc.library.enums;

public enum RecordsStatusEnum {
    RECORDSSTATUS_ALL(0, "全部"),
    RECORDSSTATUS_BORROWING(1, "出借中"),
    RECORDSSTATUS_RETURNED(2, "已歸還"),
    RECORDSSTATUS_EXPIRED(3, "過期未歸還");
    private Integer code;
    private String trans;

    public Integer getCode() {
        return code;
    }

    RecordsStatusEnum(Integer code, String trans) {
        this.code = code;
        this.trans = trans;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }
}