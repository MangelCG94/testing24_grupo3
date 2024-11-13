package com.games.games.repositories;

import com.games.games.dtos.CompraConJuegosUsuario;
import com.games.games.models.Compra;
import com.games.games.models.JuegosUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class CompraRepositoryTest {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private JuegosUsuarioRepository juegosUsuarioRepository;

    @Test
    void findByFechaCompra() {
        Compra compra1 = Compra.builder().fechaCompra(10000000L).build();
        Compra compra2 = Compra.builder().fechaCompra(20000000L).build();

        compraRepository.saveAll(List.of(compra1, compra2));

        List<Compra> compras = compraRepository.findByFechaCompra(10000000L);

        assertEquals(2, compras.size());
        assertTrue(compras.contains(compra1));
        assertTrue(compras.contains(compra2));

    }

    @Test
    void findByJuegosUsuario() {

        JuegosUsuario juegosUsuario1 = JuegosUsuario.builder().build();
        JuegosUsuario juegosUsuario2 = JuegosUsuario.builder().build();

        juegosUsuarioRepository.saveAll(List.of(juegosUsuario1, juegosUsuario2));

        Compra compra1 = Compra.builder().juegosUsuario(juegosUsuario1).build();
        Compra compra2 = Compra.builder().juegosUsuario(juegosUsuario2).build();

        compraRepository.saveAll(List.of(compra1, compra2));

        List<Compra> compras = compraRepository.findByJuegosUsuario(juegosUsuario1);

        assertEquals(1, compras.size());
        assertTrue(compras.contains(compra1));
        assertFalse(compras.contains(compra2));
    }

    @Test
    void encuentraTodasLasComprasConJuegosDeUsuario() {

        Compra compra1 = Compra.builder()
                .fechaCompra(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .juegosUsuario(JuegosUsuario.builder().build())
                .build();

        compraRepository.save(compra1);

        JuegosUsuario juegosUsuario1 = JuegosUsuario.builder()
                .id(1L)
                .time(150L)
                .build();

        JuegosUsuario juegosUsuario2 = JuegosUsuario.builder()
                .id(2L)
                .time(100L)
                .build();

        juegosUsuarioRepository.saveAll(List.of(juegosUsuario1, juegosUsuario2));

        List<CompraConJuegosUsuario> compras = compraRepository.encuentraTodasLasComprasConJuegosDeUsuario(1L, Instant.now());

        assertNotNull(compras);
        assertEquals(1, compras.size());
        CompraConJuegosUsuario dto = compras.get(0);
        assertEquals(compra1.getIdCompra(), dto.compraId());
    }

    @Test
    void finByFechaCompraEntre() {
        Compra compra1 = Compra.builder()
                .fechaCompra(Instant.now().minusSeconds(10000).toEpochMilli())
                .build();
        Compra compra2 = Compra.builder()
                .fechaCompra(Instant.now().minusSeconds(5000).toEpochMilli())
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