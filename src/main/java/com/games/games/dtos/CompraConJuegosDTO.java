package com.games.games.dtos;

public record CompraConJuegosDTO(
        Long compraId,
        Long fechaCompra,
        Long juegoId,
        String nombreJuego
) {
}
