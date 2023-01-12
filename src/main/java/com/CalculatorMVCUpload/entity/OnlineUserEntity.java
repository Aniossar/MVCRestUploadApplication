package com.CalculatorMVCUpload.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "online_user_table")
@Getter
@Setter
@NoArgsConstructor
public class OnlineUserEntity {

    @Id
    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "last_ping_time")
    private Instant lastPingTime;

}
