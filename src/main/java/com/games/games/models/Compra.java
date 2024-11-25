package com.games.games.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private Instant fechaCompra;
    @ManyToOne
    @JoinColumn(name = "juego_id")
    private Juego juego;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
