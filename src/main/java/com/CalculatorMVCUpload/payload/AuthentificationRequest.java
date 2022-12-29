package com.CalculatorMVCUpload.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthentificationRequest {

    private String login;

    private String password;

}
