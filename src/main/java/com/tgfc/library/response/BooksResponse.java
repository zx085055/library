package com.tgfc.library.response;

import org.springframework.http.ResponseEntity;

import java.util.Date;

public class BooksResponse {

    private Integer id;
    private String name;
    private String author;
    private String isbn;
    private String pubHouse;
    private Date publishDate;
    private String type;
    private String photoOriginalName;
    private Integer status;
    private String intro;
    private Integer price;
    private String photoName;
    private Date purchaseDate;
    private String page;
    private String language;

    ResponseEntity<byte[]> photo=null;

    public ResponseEntity<byte[]> getPhoto() {
        return photo;
    }

    public void setPhoto(ResponseEntity<byte[]> photo) {
        this.photo = photo;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public String getPhotoOriginalName() {
        return photoOriginalName;
    }

    public void setPhotoOriginalName(String photoOriginalName) {
        this.photoOriginalName = photoOriginalName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
