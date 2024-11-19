package com.games.games.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    private String descripcion;
    private String videoUrl;
    private Double precio;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private Date fechaLanzamiento;


    @ManyToOne
    @JoinColumn(name = "desarrolladora_id")
    private Desarrolladora desarrolladora;
}
