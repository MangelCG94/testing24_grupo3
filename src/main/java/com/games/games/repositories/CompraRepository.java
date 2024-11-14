package com.games.games.repositories;

import com.games.games.dtos.CompraConJuegosDTO;
import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByFechaCompra(Instant fechaCompra);

    List<Compra> findByFechaCompraBetween(Instant fechaCompraInicio, Instant fechaCompraFin);

    List<Compra> findByUsuarios(Usuario usuario);

    List<Compra> findByJuegos(Juego juego);

    @Query("""
    SELECT new com.games.games.controllers.dtos.CompraConJuegos(
        c.id,
        c.fechaCompra,
        j.id,
    ) FROM Compra c
    LEFT JOIN Juegos j ON c.id = j.compra.id
    WHERE c.fechaCompra = :fechaCompra AND c.idJuego = :idJuego
    GROUP BY c.id, c.fechaCompra, j.idJuego,
    """)
    List<CompraConJuegosDTO> encuentraTodasLasComprasConJuegos(@Param("idJuego") Long idJuego, @Param("fechaCompra") Instant fechaCompra);


}
