package com.games.games.dtos;

public record CompraConJuegosUsuario(
        Long compraId,
        Long fechaCompra,
        Long usuarioId,
        String nombreUsuario,
        Long juegoId,
        String nombreJuego
) {
}
