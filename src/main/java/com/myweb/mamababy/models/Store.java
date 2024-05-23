package com.myweb.mamababy.models;

import jakarta.persistence.*;
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

    @Column(name = "phone", length = 10, nullable = false)
    private String phone;

    @Column(name = "status")
    private boolean status;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
