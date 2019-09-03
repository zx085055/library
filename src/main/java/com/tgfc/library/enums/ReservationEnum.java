package com.tgfc.library.enums;

public enum ReservationEnum {
    RESERVATION_ALIVE(1,"預約未到期"),
    RESERVATION_DEAD(2,"預約已到期"),
    RESERVATION_WAIT(3,"排隊預約中"),
    RESERVATION_CANCLE(4,"預約已取消"),
    RESERVATION_SUCCESS(5,"取書完成");

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
