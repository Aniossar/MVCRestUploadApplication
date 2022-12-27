package com.CalculatorMVCUpload.payload;

public class AuthentificationResponce {

    private String token;

    public AuthentificationResponce(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
