package com.tgfc.library.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator="generatorName")
    @GenericGenerator(name = "generatorName", strategy = "native")
    private Integer bookId;
    @Column(name="name",length = 50,nullable = false)
    private String name;
    @Column(name = "author",length = 30,nullable = false)
    private String author;
    @Column(name = "isbn",length = 30,nullable = false)
    private String isbn;
    @Column(name = "pub_house",length = 30,nullable = false)
    private String pubHouse;
    @Column(name = "intro")
    private String intro;
    @Column(name = "price",length = 10,nullable = false)
    private Integer price;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "purchase_date",nullable = false)
    private Date purchaseDate;
    @Column(name = "publish_date",nullable = false)
    private Date publishDate;
    @Column(name="type",length = 20,nullable = false)
    private String type;


    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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
}
