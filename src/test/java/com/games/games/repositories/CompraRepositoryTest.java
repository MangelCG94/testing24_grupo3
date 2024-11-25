package com.games.games.repositories;

import com.games.games.dtos.CompraConJuegosDTO;
import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompraRepositoryTest {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    @BeforeEach
    void setUp(){
        compraRepository.deleteAll();
        usuarioRepository.deleteAll();
        juegoRepository.deleteAll();
    }



    @Test
    @DisplayName("Encontrar una compra por fecha de compra")
    void findByFechaCompra() {

        Instant fechaActual = Instant.now(); // Almacena el valor fijo de fecha

        Compra compra = Compra.builder().fechaCompra(fechaActual).build();

        compraRepository.save(compra);

        List<Compra> compras = compraRepository.findByFechaCompra(fechaActual);

        assertEquals(1, compras.size()); // Solo debería haber una compra con esta fecha exacta
        assertTrue(compras.contains(compra));
    }

    @Test
    @DisplayName("Encontrar una compra por usuarios")
    void findByUsuarios() {

        Usuario usuario1 = Usuario.builder().build();
        Usuario usuario2 = Usuario.builder().build();

        usuarioRepository.saveAll(List.of(usuario1, usuario2));

        Compra compra1 = Compra.builder().usuario(usuario1).build();
        Compra compra2 = Compra.builder().usuario(usuario1).build();

        compraRepository.saveAll(List.of(compra1, compra2));

        List<Compra> compras = compraRepository.findByUsuario(usuario1);

        assertEquals(2, compras.size());
        assertTrue(compras.contains(compra1));
        assertTrue(compras.contains(compra2));
    }

    @Test
    @DisplayName("Encontrar una compra por juego")
    void findByJuego() {

        Juego juego1 = Juego.builder().build();
        Juego juego2 = Juego.builder().build();

        juegoRepository.saveAll(List.of(juego1, juego2));

        Compra compra1 = Compra.builder().juego(juego1).build();
        Compra compra2 = Compra.builder().juego(juego2).build();

        compraRepository.saveAll(List.of(compra1, compra2));

        List<Compra> compras = compraRepository.findByJuego(juego1);

        assertEquals(1, compras.size());
        assertTrue(compras.contains(compra1));
        assertFalse(compras.contains(compra2));
    }

    @Test
    @DisplayName("Encontrar una compra durante un período de tiempo")
    void finByFechaCompraEntre() {
        Instant fechaCompra1 = Instant.now().minusSeconds(10000); // compra1 ocurre hace 10000 segundos
        Instant fechaCompra2 = Instant.now().minusSeconds(5000);  // compra2 ocurre hace 5000 segundos


        Compra compra1 = Compra.builder()
                .fechaCompra(fechaCompra1)
                .build();
        Compra compra2 = Compra.builder()
                .fechaCompra(fechaCompra2)
                .build();

        compraRepository.saveAll(List.of(compra1, compra2));

        Instant startDate = fechaCompra1;
        Instant endDate = fechaCompra2;

        List<Compra> compras = compraRepository.findByFechaCompraBetween(startDate, endDate);

        assertEquals(2, compras.size());
        assertTrue(compras.contains(compra1));
        assertTrue(compras.contains(compra2));
    }
}