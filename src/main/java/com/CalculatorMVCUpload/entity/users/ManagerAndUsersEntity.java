package com.CalculatorMVCUpload.entity.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "managers_users_table")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ManagerAndUsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "key_manager_id")
    private UserEntity keyManager;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
