package com.tgfc.library.enums;

public enum JobLastExecuteEnum {
    DONE("1", "成功執行"),
    FAIL("2", "執行失敗"),
    UNDONE("3", "未執行"),
    ;

    private String code;
    private String trans;

    JobLastExecuteEnum(String code, String trans) {
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
}
