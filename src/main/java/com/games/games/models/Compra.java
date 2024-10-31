package com.games.games.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private Instant fechaCompra;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Long idUsuario;
    @ManyToOne
    @JoinColumn(name = "id_juego")
    private Long idJuego;
}
