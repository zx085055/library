package com.tgfc.library.response;

import java.util.ArrayList;
import java.util.List;

public class BookCountResponse {
    private List<BooksResponse> list=new ArrayList<>();
    private int count;

    public List<BooksResponse> getList() {
        return list;
    }

    public void setList(List<BooksResponse> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
