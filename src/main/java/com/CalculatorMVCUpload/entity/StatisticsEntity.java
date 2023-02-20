package com.CalculatorMVCUpload.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "statistics_table")
@Proxy(lazy = false)
@Getter
@Setter
@NoArgsConstructor
public class StatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "time_slice")
    private Instant timeSlice;

    @Column(name = "users_online")
    private int usersOnline;

    @Column(name = "new_receipts")
    private int newReceipts;

    @Column(name = "new_users")
    private int newUsers;

}
