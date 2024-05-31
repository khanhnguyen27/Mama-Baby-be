package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brands")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;
}
