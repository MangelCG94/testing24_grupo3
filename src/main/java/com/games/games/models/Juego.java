package com.games.games.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "juegos")
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(nullable = true)
    private String descripcion;
    private String videoUrl;
    private Double precio;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private LocalDate fechaLanzamiento;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desarrolladora_id")
    private Desarrolladora desarrolladora;
}
