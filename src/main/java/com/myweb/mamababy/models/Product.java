package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;


    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "range_age")
    private Brand rangeAge;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

}
