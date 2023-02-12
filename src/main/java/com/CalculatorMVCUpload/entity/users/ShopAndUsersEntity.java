package com.CalculatorMVCUpload.entity.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shops_and_users_table")
@Getter
@Setter
@NoArgsConstructor
public class ShopAndUsersEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "shop_id")
    private int shopId;

    @Column(name = "user_id")
    private int userId;
}
