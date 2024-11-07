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
    private Integer fechaValoracion;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Long idUsuario;
    @ManyToOne
    @JoinColumn(name = "id_juego")
    private Long idJuego;
}
