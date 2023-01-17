package com.CalculatorMVCUpload.payload.request.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlockUserRequest {

    private String login;

    private boolean statusEnabled;
}
