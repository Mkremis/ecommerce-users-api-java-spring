package com.ecommerce.api.dtos;

public class LikesDTO {
    private String prodId;
    private String ProdName;
    private String prodImage;
    private String prodGender;
    private Double prodPrice;
    private String priceCurrency;

    // Constructor vacío (necesario para deserialización JSON)
    public LikesDTO(){}

    // Getters y Setters
    public String getProdId() {
        return prodId;
    }
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }

    public String getProdGender() {
        return prodGender;
    }

    public void setProdGender(String prodGender) {
        this.prodGender = prodGender;
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

    @Override
    public String toString() {
        return "LikesDTO{" +
                "prodId='" + prodId + '\'' +
                ", ProdName='" + ProdName + '\'' +
                ", prodImage='" + prodImage + '\'' +
                ", prodGender='" + prodGender + '\'' +
                ", prodPrice=" + prodPrice +
                ", priceCurrency='" + priceCurrency + '\'' +
                '}';
    }
}
