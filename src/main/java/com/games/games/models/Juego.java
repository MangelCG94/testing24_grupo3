package com.games.games.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "juegos")
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String nombre;
    private String descripcion;
    private String videoUrl;
    private Double precio;


    @ManyToOne
    @JoinColumn(name = "desarrolladora_id")
    private Desarrolladora desarrolladora;
}
