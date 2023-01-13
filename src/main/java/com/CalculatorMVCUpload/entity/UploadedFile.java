package com.CalculatorMVCUpload.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "uploadedfiles")
@Proxy(lazy = false)
@Getter
@Setter
@NoArgsConstructor
public class UploadedFile {

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

    @Column(name = "uploaddate")
    private Date uploadDate;

    @Column(name = "size")
    private long size;

    @Column(name = "hashcode")
    private int hashCode;

    @Column(name = "info")
    private String info;

    @Column(name = "for_clients")
    private String forClients;

    @Column(name = "author")
    private String author;

    public UploadedFile(String name, String path, String url, Date uploadDate, long size, int hashCode) {
        this.name = name;
        this.path = path;
        this.url = url;
        this.uploadDate = uploadDate;
        this.size = size;
        this.hashCode = hashCode;
    }

}