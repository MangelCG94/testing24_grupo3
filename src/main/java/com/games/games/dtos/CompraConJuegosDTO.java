package com.games.games.dtos;

import com.games.games.models.Juego;

import java.time.Instant;

public record CompraConJuegosDTO(
        Long compraId,
        Instant fechaCompra,
        Juego juego
) {
}
