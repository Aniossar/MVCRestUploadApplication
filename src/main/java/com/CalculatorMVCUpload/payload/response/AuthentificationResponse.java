package com.CalculatorMVCUpload.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthentificationResponse {

    private String accessToken;

    private String refreshToken;

}
