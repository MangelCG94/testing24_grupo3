package com.games.games.controllers;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.JuegosUsuario;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraControllerUTest {

    @InjectMocks
    private CompraController compraController;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private Model model;

    @Test
    void encontrarTodasCompras() {

        when(compraRepository.findAll()).thenReturn(List.of(
                Compra.builder().idCompra(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(Juego.builder().id(1L).build()).usuario(Usuario.builder().id(1L).build()).build(),
                Compra.builder().idCompra(2L).fechaCompra(Instant.ofEpochSecond(2000000000L)).juego(Juego.builder().id(2L).build()).usuario(Usuario.builder().id(1L).build()).build(),
                Compra.builder().idCompra(3L).fechaCompra(Instant.ofEpochSecond(3000000000L)).juego(Juego.builder().id(3L).build()).usuario(Usuario.builder().id(1L).build()).build()
        ));
        String view = compraController.encontrarTodasCompras(model);

        assertEquals("compra-list", view);
        verify(compraRepository).findAll();
    }

    @Test
    void encontrarPorIdCuandoExisteCompra() {

        Compra compra1 = Compra.builder().idCompra(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(Juego.builder().id(1L).build()).usuario(Usuario.builder().id(1L).build()).build();
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra1));

        String view = compraController.encontrarPorIdCompra(1L, model);

        assertEquals("compra-form", view);
        verify(compraRepository).findById(1L);
        verify(compraRepository, never()).findAll();
        verify(model).addAttribute("compra", compra1);
    }

    @Test
    void encontrarPorIdCuandoNoExisteCompra() {

        when(compraRepository.findById(1L)).thenReturn(Optional.empty());

        String view = compraController.encontrarPorIdCompra(1L, model);

        assertEquals("compra-detail", view);
        verify(compraRepository).findById(1L);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void encontrarPorId2_SiNoExisteCompra() {

        when(compraRepository.findById(1L)).thenReturn(Optional.empty());

        String view = compraController.encontrarPorIdCompra2(1L, model);

        assertEquals("error", view);
        verify(compraRepository).findById(1L);
        verify(model, never()).addAttribute(eq("compra"), any());
        verify(model).addAttribute("message", "Compra no encontrada");
    }


    @Test
    void formularioParaCrearCompra() {

        String view = compraController.formularioParaCrearCompra(model);

        assertEquals("compra-form", view);
        verify(model).addAttribute("compra", any(Compra.class));
    }

    @Test
    void formularioParaActualizarCompraSiExiste() {

        Compra compra1 = Compra.builder().idCompra(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(Juego.builder().id(1L).build()).usuario(Usuario.builder().id(1L).build()).build();
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra1));

        String view = compraController.formularioParaActualizarCompra(1L, model);

        assertEquals("compra-form", view);
        verify(compraRepository).findById(1L);
        verify(model).addAttribute("compra", compra1);
    }

    @Test
    void formularioParaActualizarCompraSiNoExiste() {

        when(compraRepository.findById(1L)).thenReturn(Optional.empty());

        String view = compraController.formularioParaActualizarCompra(1L, model);

        assertEquals("compra-form", view);
        verify(compraRepository).findById(1L);
        verify(model).addAttribute(anyString(), any());
    }


    @Test
    void guardarCompraNueva() {

        Compra compra1 = Compra.builder().idCompra(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(Juego.builder().id(1L).build()).usuario(Usuario.builder().id(1L).build()).build();

        String view = compraController.guardarCompra(compra1);

        assertEquals("redirect:/compras", view);
        verify(compraRepository).save(compra1);
    }

    @Test
    void guardarCompraExistente() {

        Compra compra1 = Compra.builder().idCompra(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(Juego.builder().id(1L).build()).usuario(Usuario.builder().id(1L).build()).build();

        Compra compraActualizada = Compra.builder().idCompra(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(Juego.builder().id(1L).build()).usuario(Usuario.builder().id(1L).build()).build();

        String view = compraController.guardarCompra(compraActualizada);

        assertEquals("redirect:/compras", view);
        verify(compraRepository).findById(1L);
        verify(compraRepository).save(compra1);
        assertEquals(compraActualizada.getJuego(), compra1.getJuego());
        assertEquals(compraActualizada.getUsuario(), compra1.getUsuario());
    }

    @Test
    void borrarPorIdCompra() {

        String view = compraController.borrarPorIdCompra(1L);

        assertEquals("redirect:/compras", view);
        verify(compraRepository).deleteById(1L);
    }

    @Test
    void borrarPorId_ErrorCapturado() {

        doThrow(new RuntimeException("Error al borrar"))
                .when(compraRepository).deleteById(1L);

        String view = compraController.borrarPorIdCompra(1L);

        assertEquals("error", view);
        verify(compraRepository).deleteById(1L);
    }

    @Test
    void borrarPorIdTodasLasCompras() {

        String view = compraController.borrarTodasLasCompras();

        assertEquals("redirect:/compras", view);
        verify(compraRepository).deleteAll();
    }

    @Test
    void borrarPorIdTodasLasCompras_ErrorCapturado() {

        doThrow(new RuntimeException("Error al borrar"))
                .when(compraRepository).deleteAll();

        String view = compraController.borrarTodasLasCompras();

        assertEquals("error", view);
        verify(compraRepository).deleteAll();
    }


}