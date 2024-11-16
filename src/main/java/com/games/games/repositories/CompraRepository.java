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

    List<Compra> findByUsuario(Usuario usuario);

    List<Compra> findByJuego(Juego juego);



}
