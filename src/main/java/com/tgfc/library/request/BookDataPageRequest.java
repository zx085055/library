package com.tgfc.library.request;

public class BookDataPageRequest extends PageableRequest{
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
