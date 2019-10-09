package com.tgfc.library.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator="generatorName")
    @GenericGenerator(name = "generatorName", strategy = "native")
    private Integer id;
    @Column(name="name",length = 100,nullable = false)
    private String name;
    @Column(name = "author",length = 100,nullable = false)
    private String author;
    @Column(name = "isbn",length = 30,nullable = false)
    private String isbn;
    @Column(name = "pub_house",length = 30,nullable = false)
    private String pubHouse;
    @Column(name = "intro")
    private String intro;
    @Column(name = "price",length = 10,nullable = false)
    private Integer price;
    @Column(name = "photo_original_name")
    private String photoOriginalName;
    @Column(name = "photo_name")
    private String photoName;
    @Column(name = "purchase_date",nullable = false,updatable = false)
//    @CreatedDate
    private Date purchaseDate;
    @Column(name = "publish_date",nullable = false)
    private Date publishDate;
    @Column(name="type",length = 20,nullable = false)
    private String type;
    @Column(name = "page",length = 10,nullable = false)
    private String page;
    @Column(name="language",length = 20,nullable = false )
    private String language;
    @Column(name="status",length = 2,nullable = false )
    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getBookId() {
        return id;
    }

    public void setBookId(Integer bookId) {
        this.id = bookId;
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

    public String getPhotoOriginalName() {
        return photoOriginalName;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public void setPhotoOriginalName(String photoUrl) {
        this.photoOriginalName = photoUrl;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
