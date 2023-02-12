package com.CalculatorMVCUpload.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MeResponse {

    private int userId;
    private String login;
    private String roleName;
    private String appAccess;
}
