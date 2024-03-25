package com.ecommerce.api.security.dtos;

import javax.validation.constraints.NotBlank;

public class LoginUser {
    @NotBlank
    private String userName;
   // @NotBlank
   // private String email;
    @NotBlank
    private String password;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

   // public String getEmail() {return email;}

    //public void setEmail(String email) {this.email = email;}

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
