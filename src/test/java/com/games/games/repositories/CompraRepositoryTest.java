package com.games.games.repositories;

import com.games.games.dtos.CompraConJuegosDTO;
import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import jakarta.transaction.Transactional;
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
        compraRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();
        juegoRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("Encontrar una compra por fecha de compra")
    void findByFechaCompra() {
        // Crear y guardar la compra
        Instant fechaCompra = Instant.now();
        Compra compra = Compra.builder().fechaCompra(fechaCompra).build();
        compraRepository.save(compra);

        // Verificar que la compra se guardó correctamente
        System.out.println("Total de compras guardadas: " + compraRepository.count());

        // Buscar compras por fecha
        List<Compra> compras = compraRepository.findByFechaCompraBetween(
                fechaCompra.minusMillis(5000), // 500ms antes
                fechaCompra.plusMillis(5000)   // 500ms después
        );
        // Asegurarse de que hay solo una compra
        assertEquals(1, compras.size());
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

        System.out.println("Total de compras guardadas: " + compraRepository.count());


        List<Compra> compras = compraRepository.findByJuego(juego1);

        assertEquals(1, compras.size());
        assertTrue(compras.contains(compra1));
        assertFalse(compras.contains(compra2));
    }

    @Test
    @DisplayName("Encontrar una compra durante un período de tiempo")
    void finByFechaCompraEntre() {
        // Definir fechas conocidas para las compras
        Instant fechaCompra1 = Instant.ofEpochSecond(1000000000L); // Un valor específico para la primera compra
        Instant fechaCompra2 = Instant.ofEpochSecond(2000000000L); // Un valor específico para la segunda compra

        // Guardar las compras en la base de datos
        Compra compra1 = Compra.builder().fechaCompra(fechaCompra1).build();
        Compra compra2 = Compra.builder().fechaCompra(fechaCompra2).build();
        compraRepository.saveAll(List.of(compra1, compra2));

        // Definir el rango de fechas con un margen de 500 milisegundos
        Instant startDate = fechaCompra1.minusMillis(500); // 500ms antes
        Instant endDate = fechaCompra2.plusMillis(500);    // 500ms después

        // Buscar compras en el rango de fechas
        List<Compra> compras = compraRepository.findByFechaCompraBetween(startDate, endDate);

        // Verificar que se encontraron las dos compras
        assertEquals(2, compras.size());
        assertTrue(compras.contains(compra1));
        assertTrue(compras.contains(compra2));
    }
}