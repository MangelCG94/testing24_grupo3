package com.games.games.controllers;

import com.games.games.models.Desarrolladora;
import com.games.games.models.Juego;
import com.games.games.repositories.DesarrolladoraRepository;
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
    private DesarrolladoraRepository desarrolladoraRepository;
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
    @DisplayName("Encontrar una id de juego que exista")
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
    @DisplayName("Encontrar una id de juego que no exista")
    void findByIdWhenJuegoNotExists() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        String view = controller.findById(1L, model);

        assertEquals("juego-detail", view);
        verify(repository).findById(1L);
        verify(model,never()).addAttribute(anyString(), any());
    }


    @Test
    @DisplayName("Guardar juego nuevo")
    void saveJuego() {
        Juego juego = Juego.builder().build();

        String view = controller.saveJuego(juego);

        assertEquals("redirect:/juegos", view);
        verify(repository).save(juego);
    }


    @Test
    @DisplayName("Guardar juego nuevo")
    void updateJuego() {
        Juego existingJuego = Juego.builder().id(1L).nombre("Mario").build();
        Juego updatedJuego = Juego.builder().id(1L).nombre("Sonic").build();

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.findById(1L)).thenReturn(Optional.of(existingJuego));

        String view = controller.saveJuego(updatedJuego);

        verify(repository).existsById(1L); // Check existence was verified
        verify(repository).findById(1L);  // Ensure it fetched the existing entity
        verify(repository).save(existingJuego); // Save is called with the modified existingJuego

        assertEquals("Sonic", existingJuego.getNombre());

        assertEquals("redirect:/juegos", view);
    }


    @Test
    @DisplayName("Crear un juego por fomulario")
    void createJuegoForm() {
        String view = controller.formCreate(model);

        assertEquals("juego-form", view);
        verify(model).addAttribute(eq("juego"), any(Juego.class));
        verify(model).addAttribute(eq("desarrolladoras"), any(List.class));
    }

    @Test
    @DisplayName("Actualizar un juego que exista")
    void updateJuegoFormIfExists() {
        Juego juego = Juego.builder().id(1L).desarrolladora(Desarrolladora.builder().build()).build();
        when(repository.findById(1L)).thenReturn(Optional.of(juego));

        String view = controller.formUpdate(model, 1L);

        assertEquals("juego-form", view);
        verify(repository).findById(1L);
        verify(model).addAttribute("juego", juego);
    }

    @Test
    @DisplayName("Actualizar un juego que no exista")
    void updateJuegoFormIfNotExists() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        String view = controller.formUpdate(model, 1L);

        assertEquals("juego-form", view);
        verify(repository).findById(1L);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    @DisplayName("Borrar un usuario")
    void borrarPorId() {

        String view = controller.deleteJuego(1L);

        assertEquals("redirect:/juegos", view);
        verify(repository).deleteById(1L);
    }
}
