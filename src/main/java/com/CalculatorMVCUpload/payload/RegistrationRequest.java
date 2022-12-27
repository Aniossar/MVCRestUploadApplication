package com.CalculatorMVCUpload.payload;

public class RegistrationRequest {

    public enum DesiredRole{
        SUPPLIER, SHOP, USER
    }

    private String login;
    private String password;
    private String email;
    private DesiredRole desiredRole;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String login, String password, String email, DesiredRole desiredRole) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.desiredRole = desiredRole;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DesiredRole getDesiredRole() {
        return desiredRole;
    }

    public void setDesiredRole(DesiredRole desiredRole) {
        this.desiredRole = desiredRole;
    }
}
