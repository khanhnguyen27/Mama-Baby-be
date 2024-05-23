package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order_id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product_id;

    @Column(name = "quality", nullable = false)
    private int quality;

    @Column(name = "unit_price", nullable = false)
    private float unit_price;

    @Column(name = "unit_point", nullable = false)
    private int unit_point;

    @Column(name = "amount", nullable = false)
    private float amount;






}
