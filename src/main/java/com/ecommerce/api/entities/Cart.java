package com.ecommerce.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.ecommerce.api.security.entities.User;


import javax.persistence.*;


@Entity
@Table(name = "users_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String prodId;
    private String prodGender;
    private String prodName;
    private String prodImage;
    private Double prodPrice;
    private String priceCurrency;
    private int productQ;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Quitamos la restricción unique aquí
    private User user;


    public Cart(){}
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdGender() {
        return prodGender;
    }

    public void setProdGender(String prodGender) {
        this.prodGender = prodGender;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }

    public Double getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(Double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getProductQ() {
        return productQ;
    }

    public void setProductQ(int productQ) {
        this.productQ = productQ;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
