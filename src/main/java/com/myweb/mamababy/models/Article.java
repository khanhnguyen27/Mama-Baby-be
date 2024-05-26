package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.util.Date;

@Entity
@Table(name = "articles")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "header", nullable = false)
    private String header;

    @Column(name = "content")
    private String content;

    @Column(name = "link_product")
    private String link_product;

    @Column(name = "link_image")
    private String link_image;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "date")
    private Date date;

    @Column(name = "status")
    private Boolean status;

}
