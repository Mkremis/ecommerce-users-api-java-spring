package com.ecommerce.api.security.dtos;

import com.ecommerce.api.entities.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class LoginResponse {
    private Message message;
    private HashMap<String, String> userData;
    private Set userCart;
    private Set userLikes;

    public LoginResponse() {}

    public LoginResponse(Message message, HashMap<String, String> userData, Set userCart, Set userLikes) {
        this.message = message;
        this.userData = userData;
        this.userCart = userCart;
        this.userLikes = userLikes;
    }

    // Getters and setters


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public HashMap<String, String> getUserData() {
        return userData;
    }

    public void setUserData(HashMap<String, String> userData) {
        this.userData = userData;
    }

    public Set getUserCart() {
        return userCart;
    }

    public void setUserCart(Set userCart) {
        this.userCart = userCart;
    }

    public Set getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Set userLikes) {
        this.userLikes = userLikes;
    }
}