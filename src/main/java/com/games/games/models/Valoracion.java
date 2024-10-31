package com.games.games.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer valoracion;
    @ManyToOne
    private Juego juego;
    @ManyToOne
    private Usuario usuario;
}
