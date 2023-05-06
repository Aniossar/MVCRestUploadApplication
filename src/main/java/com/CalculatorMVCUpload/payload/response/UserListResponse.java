package com.CalculatorMVCUpload.payload.response;

import com.CalculatorMVCUpload.entity.users.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {

    private int id;

    private String login;

    private String fullName;

    private String companyName;

    private String certainPlaceAddress;

    private int keyManagerId;

    private RoleEntity roleEntity;
}
