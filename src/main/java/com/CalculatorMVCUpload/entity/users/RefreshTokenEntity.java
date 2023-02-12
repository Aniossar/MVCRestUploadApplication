package com.CalculatorMVCUpload.entity.users;

import javax.persistence.Column;
import javax.persistence.Id;

public class RefreshTokenEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "refresh_token")
    private String refreshToken;
}
