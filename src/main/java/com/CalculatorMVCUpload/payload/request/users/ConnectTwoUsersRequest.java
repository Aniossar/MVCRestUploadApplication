package com.CalculatorMVCUpload.payload.request.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectTwoUsersRequest {

    private int userIdMain;

    private int userIdNonMain;
}
