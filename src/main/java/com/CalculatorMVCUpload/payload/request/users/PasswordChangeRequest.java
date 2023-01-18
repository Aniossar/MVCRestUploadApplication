package com.CalculatorMVCUpload.payload.request.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordChangeRequest {

    private String oldPassword;
    private String newPassword;

}
