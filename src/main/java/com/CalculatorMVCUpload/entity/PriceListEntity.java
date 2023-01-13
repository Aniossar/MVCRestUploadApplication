package com.CalculatorMVCUpload.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pricelist_table")
@Getter
@Setter
@NoArgsConstructor
public class PriceListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "url")
    private String url;

    @Column(name = "uploadtime")
    private Instant uploadTime;

    @Column(name = "info")
    private String info;

    @Column(name = "for_clients")
    private String forClients;

    @Column(name = "author")
    private String userAuthor;

    public PriceListEntity(String name, String path, String url, Instant uploadTime,
                           String info, String forClients, String userAuthor) {
        this.name = name;
        this.path = path;
        this.url = url;
        this.uploadTime = uploadTime;
        this.info = info;
        this.forClients = forClients;
        this.userAuthor = userAuthor;
    }
}
