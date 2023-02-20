package com.CalculatorMVCUpload.payload.request.users;

import com.CalculatorMVCUpload.entity.users.RoleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
public class UserEditRequest {

    public enum NewRole {
        ADMIN, MODERATOR, SUPPLIER, KEYMANAGER, SHOP, USER
    }

    private int userId;

    private String login;

    private String email;

    private String fullName;

    private String companyName;

    private String phoneNumber;

    private String address;

    private String certainPlaceAddress;

    private String appAccess;

    private NewRole newRole;

    private Boolean enabled;
}
