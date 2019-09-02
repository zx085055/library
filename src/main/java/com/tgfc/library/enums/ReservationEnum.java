package com.tgfc.library.enums;

public enum ReservationEnum {
    RESERVATION_ALIVE(1,"預約未到期"),
    RESERVATION_DEAD(2,"預約已到期");

    private Integer code;
    private String trans;

    public Integer getCode() {
        return code;
    }

    ReservationEnum(Integer code, String trans){
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
