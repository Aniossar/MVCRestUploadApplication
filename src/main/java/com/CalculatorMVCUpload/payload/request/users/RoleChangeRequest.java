package com.CalculatorMVCUpload.payload.request.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleChangeRequest {

    public enum NewRole {
        MODERATOR, SUPPLIER, SHOP, USER
    }

    private String login;

    private NewRole newRole;
}
