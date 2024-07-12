package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stores")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_store", nullable = false, length = 255)
    private String nameStore;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "license_url")
    private String licenseUrl ;

    @Column(name = "status")
    private String status;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "valid_date")
    private LocalDateTime validDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
