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
    private int totalPoint;

    @Column(name = "amount")
    private float amount;

    @Column(name = "total_discount")
    private float totalDiscount;

    @Column(name = "final_amount")
    private float finalAmount;

    @Column(name = "shipping_address", nullable = false, length = 100)
    private String shippingAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "type")
    private String type;

    @Column(name = "oder_date")
    private LocalDate oderDate;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    @JsonBackReference
    private Voucher voucher;
}

