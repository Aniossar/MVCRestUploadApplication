package com.CalculatorMVCUpload.entity.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "managers_users_table")
@Getter
@Setter
@NoArgsConstructor
public class ManagerAndUsersEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "key_manager_id")
    private int keyManagerId;

    @Column(name = "user_id")
    private int userId;
}
