package com.CalculatorMVCUpload.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "claims_table")
@Proxy(lazy = false)
@Getter
@Setter
@NoArgsConstructor
public class ClaimEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "timeDate")
    private Instant timeDate;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "url")
    private String url;

    @Column(name = "responsible_user_id")
    private int responsibleUserId;

    @Column(name = "is_Solved")
    private Boolean isSolved;

}
