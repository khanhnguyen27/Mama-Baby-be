package com.myweb.mamababy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exchange_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    @JsonBackReference
    private Exchange exchange;
}
