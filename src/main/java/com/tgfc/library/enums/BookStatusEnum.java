package com.tgfc.library.enums;

public enum BookStatusEnum {
    BOOK_STATUS_LEND(1, "在館中"),
    BOOK_STATUS_INSIDE(2, "出借中"),
    BOOK_STATUS_LOST(3, "遺失"),
    BOOK_STATUS_BROKEN(4, "破損"),
    BOOK_STATUS_NOT_RETURNED(5, "未歸還");

    private Integer code;
    private String trans;
    BookStatusEnum(Integer code, String trans) {
        this.code = code;
        this.trans = trans;
    }

    public Integer getCode() {
        return code;
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

    public static BookStatusEnum getStatus(Integer i){
        switch(i){
            case 1:
                return BOOK_STATUS_LEND;
            case 2:
                return BOOK_STATUS_INSIDE;
            case 3:
                return BOOK_STATUS_LOST;
            case 4:
                return BOOK_STATUS_BROKEN;
            default:
                return null;
        }
    }

}
