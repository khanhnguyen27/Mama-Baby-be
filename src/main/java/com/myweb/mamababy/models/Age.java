package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "age")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Age {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "range_age", nullable = false)
    private String rangeAge;
}
