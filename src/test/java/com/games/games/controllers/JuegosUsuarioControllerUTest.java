package com.games.games.controllers;

import com.games.games.models.JuegosUsuario;
import com.games.games.repositories.JuegosUsuarioRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuegosUsuarioControllerTest {

    @Mock
    private JuegosUsuarioRepository juegosUsuarioRepository;

    @Mock
    private JuegoRepository juegoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Model model;

    @InjectMocks
    private JuegosUsuarioController controller;

    @Test
    @DisplayName("Encontrar todos los juegos de usuario")
    void findAll() {
        when(juegosUsuarioRepository.findAll()).thenReturn(Collections.emptyList());

        String view = controller.findAll(model);

        verify(model).addAttribute("title", "Lista de Juegos de Usuario");
        verify(model).addAttribute("juegosUsuarios", Collections.emptyList());
        assertEquals("juegosUsuario-list", view);
    }

    @Test
    @DisplayName("Encontrar juegos de usuario por id")
    void findById() {
        JuegosUsuario juegosUsuario = new JuegosUsuario();
        when(juegosUsuarioRepository.findById(1L)).thenReturn(Optional.of(juegosUsuario));

        String view = controller.findById(1L, model);

        verify(model).addAttribute("juegosUsuario", juegosUsuario);
        assertEquals("juegosUsuario-detail", view);
    }

    @Test
    @DisplayName("Buscar juegos de usuario por id que no existe")
    void findByIdNotFound() {
        when(juegosUsuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> controller.findById(1L, model));
    }

    @Test
    @DisplayName("Actualizar por formulario")
    void formUpdate() {
        JuegosUsuario juegosUsuario = new JuegosUsuario();
        when(juegosUsuarioRepository.findById(1L)).thenReturn(Optional.of(juegosUsuario));
        when(juegoRepository.findAll()).thenReturn(Collections.emptyList());
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        String view = controller.formUpdate(1L, model);

        verify(model).addAttribute("juegosUsuario", juegosUsuario);
        verify(model).addAttribute("juegos", Collections.emptyList());
        verify(model).addAttribute("usuarios", Collections.emptyList());
        assertEquals("juegosUsuario-form", view);
    }

    @Test
    @DisplayName("Actualizar un juego de usuario que existe")
    void saveUpdate() {
        JuegosUsuario existing = new JuegosUsuario();
        existing.setId(1L);
        JuegosUsuario juegosUsuario = new JuegosUsuario();
        juegosUsuario.setId(1L);

        when(juegosUsuarioRepository.existsById(1L)).thenReturn(true);
        when(juegosUsuarioRepository.findById(1L)).thenReturn(Optional.of(existing));

        String view = controller.save(juegosUsuario);

        verify(juegosUsuarioRepository).save(existing);
        assertEquals("redirect:/juegosUsuario", view);
    }

    @Test
    @DisplayName("Borrar un juego de usuario que existe")
    void delete() {
        doNothing().when(juegosUsuarioRepository).deleteById(1L);

        String view = controller.delete(1L);

        verify(juegosUsuarioRepository).deleteById(1L);
        assertEquals("redirect:/juegosUsuario", view);
    }

    @Test
    @DisplayName("Borrar un juego de usuario que no existe")
    void deleteError() {
        doThrow(new RuntimeException()).when(juegosUsuarioRepository).deleteById(1L);

        String view = controller.delete(1L);

        verify(juegosUsuarioRepository).deleteById(1L);
        assertEquals("error", view);
    }
}
