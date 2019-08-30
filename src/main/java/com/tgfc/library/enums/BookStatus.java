package com.tgfc.library.enums;

public enum BookStatus {
    LEND("1", "出借中"),
    INSIDE("2", "在館中"),
    LOST("3", "遺失"),
    Broken("4", "破損");

    private String code;
    private String trans;
    BookStatus(String code, String trans) {
        this.code = code;
        this.trans = trans;
    }

    public static BookStatus getStatus(String i){
        switch(i){
            case "1":
                return LEND;
            case "2":
                return INSIDE;
            case "3":
                return LOST;
            case "4":
                return Broken;
            default:
                return null;
        }
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
