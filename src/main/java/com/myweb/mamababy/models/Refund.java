package com.myweb.mamababy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "refunds")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;

    @Column(name = "amount")
    private float amount;

    @Column(name = "create_date")
    private LocalDate createDate;

}
