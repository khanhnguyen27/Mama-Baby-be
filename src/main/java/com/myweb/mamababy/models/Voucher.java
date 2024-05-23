package com.myweb.mamababy.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @JoinColumn(name = "store_id")
    private Store store_id;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_value")
    private int discount_value;

    @Column(name = "description", length = 100)
    private String description;

    @JsonProperty("end_at")
    private Date end_at;

}
