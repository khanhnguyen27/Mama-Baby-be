package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_value")
    private int discountValue;

    @Column(name = "description")
    private String description;

    @Column(name = "end_at")
    private LocalDate endAt;

}
