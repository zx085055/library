package com.tgfc.library.enums;

public enum RecommendEnum {
    RECOMMEND_ALIVE(1,"推薦中"),
    RECOMMEND_END(2,"已採購");
    private Integer code;
    private String trans;

    public Integer getCode() {
        return code;
    }

    RecommendEnum(Integer code, String trans){
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
