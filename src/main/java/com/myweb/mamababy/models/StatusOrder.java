package com.myweb.mamababy.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "status_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class  StatusOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private LocalDateTime date;
}
