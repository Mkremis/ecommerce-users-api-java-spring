package com.ecommerce.api.dtos;

public class ProductDTO {
    private String prodId;
    private String prodName;
    private String prodGender;
    private String prodImage;
    private Double prodPrice;
    private String priceCurrency;
    private int productQ;

    // Constructor vacío (necesario para deserialización JSON)
    public ProductDTO() {
    }

    // Getters y Setters


    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdGender() {
        return prodGender;
    }

    public void setProdGender(String prodGender) {
        this.prodGender = prodGender;
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

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public int getProductQ() {
        return productQ;
    }

    public void setProductQ(int productQ) {
        this.productQ = productQ;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "prodId='" + prodId + '\'' +
                ", prodName='" + prodName + '\'' +
                ", prodGender='" + prodGender + '\'' +
                ", prodImage='" + prodImage + '\'' +
                ", prodPrice=" + prodPrice +
                ", priceCurrency='" + priceCurrency + '\'' +
                ", productQ=" + productQ +
                '}';
    }
}

