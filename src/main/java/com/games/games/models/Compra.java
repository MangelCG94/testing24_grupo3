package com.games.games.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

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
    private Long idCompra;
    @CreatedDate
    private Long fechaCompra;
    @ManyToOne
    @JoinColumn(name = "id_juegos_de_usuario")
    private JuegosUsuario juegosUsuario;

}
