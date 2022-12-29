package com.CalculatorMVCUpload.payload;

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

    private DesiredRole desiredRole;


}
