package com.tgfc.library.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class BookAddRequest {
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String author;
    @NotNull
    private String isbn;
    @NotNull
    private String pubHouse;
//    @NotNull
    private String intro;
    @NotNull
    private Integer price;
    @NotNull
    private Date publishDate;
    @NotNull
    private Date purchaseDate;
    @NotNull
    private String type;
    @NotNull
    private String language;
    private String photoOriginalName;
    @NotNull
    private Integer status;

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPubHouse() {
        return pubHouse;
    }

    public void setPubHouse(String pubHouse) {
        this.pubHouse = pubHouse;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPhotoOriginalName() {
        return photoOriginalName;
    }

    public void setPhotoOriginalName(String photoOriginalName) {
        this.photoOriginalName = photoOriginalName;
    }


}
