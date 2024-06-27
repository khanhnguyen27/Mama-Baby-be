package com.myweb.mamababy.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
