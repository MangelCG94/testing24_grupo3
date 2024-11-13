package com.games.games.repositories;

import com.games.games.dtos.CompraConJuegosDTO;
import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.JuegosUsuario;
import com.games.games.models.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompraRepositoryTest {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    @Test
    void findByFechaCompra() {
        Compra compra1 = Compra.builder().fechaCompra(Instant.now()).build();
        Compra compra2 = Compra.builder().fechaCompra(Instant.ofEpochSecond(10000)).build();

        compraRepository.saveAll(List.of(compra1, compra2));

        List<Compra> compras = compraRepository.findByFechaCompra(Instant.now());

        assertEquals(2, compras.size());
        assertTrue(compras.contains(compra1));
        assertTrue(compras.contains(compra2));

    }

    @Test
    void findByUsuarios() {

        Usuario usuario1 = Usuario.builder().build();
        Usuario usuario2 = Usuario.builder().build();

        usuarioRepository.saveAll(List.of(usuario1, usuario2));

        Compra compra1 = Compra.builder().usuario(usuario1).build();
        Compra compra2 = Compra.builder().usuario(usuario1).build();

        compraRepository.saveAll(List.of(compra1, compra2));

        List<Compra> compras = compraRepository.findByUsuarios(usuario1);

        assertEquals(1, compras.size());
        assertTrue(compras.contains(compra1));
        assertFalse(compras.contains(compra2));
    }

    @Test
    void findByJuegos() {

        Juego juego1 = Juego.builder().build();
        Juego juego2 = Juego.builder().build();

        juegoRepository.saveAll(List.of(juego1, juego2));

        Compra compra1 = Compra.builder().juego(juego1).build();
        Compra compra2 = Compra.builder().juego(juego2).build();

        compraRepository.saveAll(List.of(compra1, compra2));

        List<Compra> compras = compraRepository.findByJuegos(juego1);

        assertEquals(1, compras.size());
        assertTrue(compras.contains(compra1));
        assertFalse(compras.contains(compra2));
    }

    @Test
    void encuentraTodasLasComprasConJuegos() {

        Juego juego1 = Juego.builder()
                .id(1L)
                .nombre("The legend of Zelda")
                .build();

        Juego juego2 = Juego.builder()
                .id(2L)
                .nombre("FIFA 14")
                .build();

        juegoRepository.saveAll(List.of(juego1, juego2));

        Compra compra1 = Compra.builder()
                .fechaCompra(Instant.ofEpochSecond(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()))
                .juego(juego1)
                .build();

        compraRepository.save(compra1);

        List<CompraConJuegosDTO> compras = compraRepository.encuentraTodasLasComprasConJuegos(1L, Instant.now());

        assertNotNull(compras);
        assertEquals(1, compras.size());
        CompraConJuegosDTO dto = compras.get(0);
        assertEquals(compra1.getIdCompra(), dto.compraId());
    }

    @Test
    void finByFechaCompraEntre() {
        Compra compra1 = Compra.builder()
                .fechaCompra(Instant.ofEpochSecond(Instant.now().minusSeconds(10000).toEpochMilli()))
                .build();
        Compra compra2 = Compra.builder()
                .fechaCompra(Instant.ofEpochSecond(Instant.now().minusSeconds(5000).toEpochMilli()))
                .build();

        compraRepository.saveAll(List.of(compra1, compra2));

        Instant startDate = Instant.now().minusSeconds(15000);
        Instant endDate = Instant.now();

        List<Compra> compras = compraRepository.findByFechaCompraEntre(startDate, endDate);

        assertEquals(2, compras.size());
        assertTrue(compras.contains(compra1));
        assertTrue(compras.contains(compra2));
    }
}