package com.tgfc.library.request;

public class BookDataPageRequest extends PageableRequest {
    private String keyword;
    private Boolean checkPermission;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getCheckPermission() {
        return checkPermission;
    }

    public void setCheckPermission(Boolean checkPermission) {
        this.checkPermission = checkPermission;
    }
}
