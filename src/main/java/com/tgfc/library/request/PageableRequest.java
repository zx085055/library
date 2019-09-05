package com.tgfc.library.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

public class PageableRequest {

    private  int pageSize=10;

    private  int pageNumber=0;

    private String sortType="ASC";

    private String sortBy="id";

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Pageable getPageable(){
        Sort sort = new Sort(getDirection(),getSortBy());
        return PageRequest.of(getPageNumber(),getPageSize(),sort);
    }

    private Sort.Direction getDirection(){
        if(this.sortType.equals(Sort.Direction.ASC.toString())){
            return Sort.Direction.ASC;
        }else{
            return Sort.Direction.DESC;
        }

    }
}
