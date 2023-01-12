package com.CalculatorMVCUpload.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PasswordResetRequest {

    private String restoreToken;

    private String newPassword;
}
