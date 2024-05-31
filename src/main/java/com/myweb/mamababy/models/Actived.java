package com.myweb.mamababy.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "actived")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ActivedKey.class)
@Builder
public class Actived {

    @Id
    private int voucherId;

    @Id
    private int userId;
}
