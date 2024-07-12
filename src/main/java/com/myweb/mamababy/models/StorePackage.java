package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "store_package")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "price")
    private float price;

    @Column(name = "buy_date")
    private LocalDateTime buyDate;

    @Column(name = "valid_date")
    private LocalDateTime validDate;
}
