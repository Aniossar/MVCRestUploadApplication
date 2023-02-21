package com.CalculatorMVCUpload.payload.response;

import com.CalculatorMVCUpload.entity.users.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private int id;

    private String login;

    private String email;

    private String fullName;

    private String companyName;

    private String phoneNumber;

    private String address;

    private String certainPlaceAddress;

    private String appAccess;

    private RoleEntity roleEntity;

    private int keyManagerId;

    private boolean enabled;
}
