package com.games.games.controllers;

import com.games.games.models.Desarrolladora;
import com.games.games.models.Juego;
import com.games.games.repositories.JuegoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JuegoControllerUTest {
    @InjectMocks
    private JuegoController controller;
    @Mock
    private JuegoRepository repository;
    @Mock
    private Model model;

    @Test
    @DisplayName("Encontrar una lista de juegos")
    void findAllTest() {
        when(repository.findAll()).thenReturn(List.of(
                Juego.builder().build(),
                Juego.builder().build()));

        String view = controller.findAll(model);

        assertEquals("juego-list", view);
        verify(repository).findAll();
    }

    @Test
    void findByIdWhenJuegoExists() {
        Juego juego = Juego.builder().id(1L).desarrolladora(Desarrolladora.builder().nombreCom("empresa").build()).build();
        when(repository.findById(1L)).thenReturn(Optional.of(juego));

        String view = controller.findById(1L, model);

        assertEquals("juego-detail", view);
        verify(repository).findById(1L);
        verify(repository, never()).findAll();
        verify(model).addAttribute("juego", juego);
    }

    @Test
    void findByIdWhenJuegoNotExists() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        String view = controller.findById(1L, model);

        assertEquals("juego-detail", view);
        verify(repository).findById(1L);
        verify(model,never()).addAttribute(anyString(), any());
    }

    @Test
    void createJuegoForm() {
        String view = controller.formCreate(model);

        assertEquals("juego-form", view);
        verify(model).addAttribute(eq("juego"), Juego.class);
    }

    @Test
    @DisplayName("Formulario para actualizar una compra si ésta existe")
    void formularioParaActualizarCompraSiExiste() {
        Juego juego = Juego.builder().id(1L).desarrolladora(Desarrolladora.builder().build()).build();
        when(repository.findById(1L)).thenReturn(Optional.of(juego));

        String view = controller.formUpdate(model, 1L);

        assertEquals("juego-form", view);
        verify(repository).findById(1L);
        verify(model).addAttribute("juego", juego);
    }

    @Test
    @DisplayName("Formulario para actualizar una compra si ésta no existe")
    void formularioParaActualizarCompraSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        String view = controller.formUpdate(model, 1L);

        assertEquals("error", view);
        verify(repository).findById(1L);
        verify(model).addAttribute(eq("mensaje"), eq("Compra no encontrada"));

        verify(model, never()).addAttribute(eq("compra"), any());
        verify(model, never()).addAttribute(eq("juegos"), any());
        verify(model, never()).addAttribute(eq("usuarios"), any());

    }
}
