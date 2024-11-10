package com.games.games.repositories;

import com.games.games.dtos.CompraConJuegosUsuario;
import com.games.games.models.Compra;
import com.games.games.models.JuegosUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByFechaCompra(Instant fechaCompra);

    List<Compra> findByJuegosUsuario(JuegosUsuario juegosUsuario);


    @Query("""
    SELECT new com.games.games.controllers.dtos.CompraConJuegosUsuario(
        c.id,
        c.fechaCompra,
        c.idUsuario,
        u.nombreUsuario,
        c.idJuego,
        j.nombreJuego
    ) FROM Compra c
    LEFT JOIN JuegosUsuario ju ON c.idJuego = j.id
    LEFT JOIN Juegos j ON c.idJuego = j.id
    LEFT JOIN Usuario u ON c.idUsuario = u.id
    WHERE c.fechaCompra = :fechaCompra AND c.idUsuario = :idUsuario
    GROUP BY c.id, c.fechaCompra, u.nombreUsuario, j.nombreJuego,
    """)
    List<CompraConJuegosUsuario> encuentraTodasLasComprasConJuegosDeUsuario(@Param("idUsuario") Long idUsuario, @Param("fechaCompra") Instant fechaCompra);
}
