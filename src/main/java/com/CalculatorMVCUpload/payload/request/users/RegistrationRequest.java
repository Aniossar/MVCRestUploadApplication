package com.CalculatorMVCUpload.payload.request.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    private String appAccess;

    private DesiredRole desiredRole;

}
