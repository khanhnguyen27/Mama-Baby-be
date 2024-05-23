package com.myweb.mamababy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(name = "total_point")
    private int total_point;

    @Column(name = "amount")
    private float amount;

    @Column(name = "total_discount")
    private float total_discount;

    @Column(name = "final_amount")
    private float final_amount;

    @Column(name = "shipping_address", nullable = false, length = 100)
    private String shipping_address;

    @Column(name = "payment_method")
    private String payment_method;

    @Column(name = "type")
    private String type;

    @Column(name = "oder_date")
    private LocalDate oder_date;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    @JsonBackReference
    private Voucher voucher_id;
}

