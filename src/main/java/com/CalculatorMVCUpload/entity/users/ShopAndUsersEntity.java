package com.CalculatorMVCUpload.entity.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "shops_users_table")
@Getter
@Setter
@NoArgsConstructor
public class ShopAndUsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "shop_id")
    private UserEntity shop;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
