package com.CalculatorMVCUpload.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    public enum DesiredRole{
        SUPPLIER, SHOP, USER
    }

    private String login;

    private String password;

    private String email;

    private String fullName;

    private String companyName;

    private String phoneNumber;

    private String address;

    private String certainPlaceAddress;

    private DesiredRole desiredRole;

}
