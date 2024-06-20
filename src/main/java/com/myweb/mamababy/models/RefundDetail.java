package com.myweb.mamababy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refund_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private float unitPrice;

    @Column(name = "amount ", nullable = false)
    private float amount;

    @ManyToOne
    @JoinColumn(name = "refund_id")
    @JsonBackReference
    private Refund refund;
}
