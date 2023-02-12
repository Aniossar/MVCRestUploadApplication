package com.CalculatorMVCUpload.entity.users;

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

   /* @Id
    @Column(name = "id")
    private int id;*/

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "last_ping_time")
    private Instant lastPingTime;

}
