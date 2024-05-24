package com.myweb.mamababy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private Order orderId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product_id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private float unitPrice;

    @Column(name = "unit_point", nullable = false)
    private int unitPoint;

    @Column(name = "amount", nullable = false)
    private float amount;






}
