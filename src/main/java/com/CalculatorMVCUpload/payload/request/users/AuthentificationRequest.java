package com.CalculatorMVCUpload.payload.request.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AuthentificationRequest {

    private String login;

    private String password;

}
