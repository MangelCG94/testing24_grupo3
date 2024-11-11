package com.games.games.services;

import com.games.games.models.Compra;
import com.games.games.models.JuegosUsuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegosUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private JuegosUsuarioRepository juegosUsuarioRepository;

    @InjectMocks
    private CompraService compraService;

    private Compra compra;
    private JuegosUsuario juegosUsuario;

    @BeforeEach
    void setUp() {
        compra = Compra.builder()
                .idCompra(1L)
                .fechaCompra(1677721600L)
                .juegosUsuario(juegosUsuario)
                .build();

        juegosUsuario = JuegosUsuario.builder()
                .id(1L)
                .time(150L)
                .build();
    }

    @Test
    void getCompraByIdEncontrada() {
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));

        Compra result = compraService.getCompraById(1L);

        assertNotNull(result,"El resultado no debería ser nulo");
        assertEquals(1L, result.getIdCompra(), "El ID de la compra debería ser 1");
        assertEquals(1677721600L, result.getFechaCompra(), "La fecha de compra debería ser 1677721600");

        verify(compraRepository, times(1)).findById(1L);
    }

    @Test
    void getCompraByIdNoEncontrada() {
        when(compraRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            compraService.getCompraById(1L);
        }, "Se debería lanzar una excepción cuando no se encuentra la compra.");

        assertEquals("Compra no encontrada", exception.getMessage(), "El mensaje de la excepción debería ser 'Compra no encontrada'");

        verify(compraRepository, times(1)).findById(1L);
    }

    @Test
    void getAllCompras() {
        when(compraRepository.findAll()).thenReturn(Arrays.asList(compra));

        List<Compra> resultado = compraService.getAllCompras();

        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1, resultado.size(), "Debería haber una compra en la lista");
        assertEquals(compra, resultado.get(0), "La compra en la lista debería ser igual a la compra de prueba");

        verify(compraRepository, times(1)).findAll();
    }

    @Test
    void getComprasByFecha() {
        when(compraRepository.findByJuegosUsuario(juegosUsuario)).thenReturn(Arrays.asList(compra));

        List<Compra> resultado = compraService.getComprasByJuegosUsuario(juegosUsuario);

        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1, resultado.size(), "Debería haber una compra en la lista");
        assertEquals(compra, resultado.get(0), "La compra en la lista debería ser igual a la compra de prueba");

        verify(compraRepository, times(1)).findByJuegosUsuario(juegosUsuario);
    }

    @Test
    void findByFechaCompraEntreFechas() {

        Instant startDate = Instant.now().plusSeconds(10000);
        Instant endDate = Instant.now().plusSeconds(160000);

        when(compraRepository.findByFechaCompraEntre(startDate, endDate)).thenReturn(Arrays.asList(compra));

        List<Compra> resultado = compraService.findByFechaCompraEntreFechas(startDate, endDate);

        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1, resultado.size(), "Debería haber una compra en la lista");
        assertEquals(compra, resultado.get(0), "La compra en la lista debería ser igual a la compra de prueba");

        verify(compraRepository, times(1)).findByFechaCompraEntre(startDate, endDate);
    }

    @Test
    void hazCompra() {
        when(compraRepository.save(any(Compra.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Compra resultado = compraService.hazCompra(1677721600L, 1L);

        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1L, resultado.getIdCompra(), "El ID de la compra debería ser 1");
        assertEquals(1677721600L, resultado.getFechaCompra(), "La fecha de compra debería ser 1677721600");

        verify(compraRepository, times(1)).save(any(Compra.class));
    }

    @Test
    void cancelaCompra() {

        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));

        compraService.cancelaCompra(1L);

        verify(compraRepository, times(1)).findById(1L);
        verify(compraRepository, times(1)).delete(compra);
    }


}