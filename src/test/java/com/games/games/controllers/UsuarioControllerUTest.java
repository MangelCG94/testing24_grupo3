package com.games.games.controllers;

import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private Model model;



    @Test
    void encontrarTodos() {

        when(usuarioRepository.findAll()).thenReturn(List.of(

                Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(2L).nombreUsuario("Pedro").password("1234").nombre("Pedro").direccion("Calle 2").CP(12334).DNI("19797477M").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(3L).nombreUsuario("Carlos").password("1234").nombre("Carlos").direccion("Calle 3").CP(44147).DNI("13464497M").fechaCreacion(Date.from(Instant.now())).build()
        ));

        String view = usuarioController.encontrarTodos(model);

        assertEquals("usuario-list", view);
        verify(usuarioRepository).findAll();

    }

    @Test
    void encontrarPorIdCuandoExisteUsuario() {

        Usuario juan = Usuario.builder().id(1L).nombreUsuario("Juan").build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(juan));

        String view = usuarioController.encontrarPorId((1L), model);

        assertEquals("usuario-detail", view);
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository, never()).findAll();
        verify(model).addAttribute("usuario", juan);
    }

    @Test
    void encontrarPorIdCuandoNoExisteUsuario() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        String view = usuarioController.encontrarPorId(1L, model);

        assertEquals("usuario-detail", view);
        verify(usuarioRepository).findById(1L);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void encontrarPorId2_siNoExisteUsuario() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        String view = usuarioController.encontrarPorId2(1L, model);

        assertEquals("error", view);
        verify(usuarioRepository).findById(1L);
        verify(model, never()).addAttribute(eq("usuario"), any());
        verify(model).addAttribute("mensaje", "Usuario no encontrado");
    }


    @Test
    void formularioParaCrear() {

        String view = usuarioController.formularioParaCrear(model);

        assertEquals("usuario-form", view);
        verify(model).addAttribute(eq("usuario"), any(Usuario.class));



    }

    @Test
    void formularioParaActualizarSiExiste() {

        Usuario juan = Usuario.builder().id(1L).nombreUsuario("Juan").build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(juan));

        String view = usuarioController.formularioParaActualizar((1L), model);

        assertEquals("usuario-form", view);
        verify(usuarioRepository).findById(1L);
        verify(model).addAttribute("usuario", juan);

    }

    @Test
    void formularioParaActualizarSiNoExiste() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        String view = usuarioController.formularioParaActualizar(1L, model);

        assertEquals("usuario-form", view);
        verify(usuarioRepository).findById(1L);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void guardarUsuarioNuevo() {

        Usuario usuario = new Usuario();

        String view = usuarioController.guardar(usuario);

        assertEquals("redirect:/usuarios", view);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void guardarUsuarioExistente() {

        Usuario usuario = Usuario.builder().id(1L).nombre("Juan").build();

        Usuario usuarioActualizado = Usuario.builder().id(1L).nombre("José").build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        String view = usuarioController.guardar(usuarioActualizado);

        assertEquals("redirect:/usuarios", view);
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(usuario);
        assertEquals(usuarioActualizado.getNombre(), usuario.getNombre());
    }


    @Test
    void borrarPorId() {

        String view = usuarioController.borrarPorId(1L);

        assertEquals("redirect:/usuarios", view);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void borrarPorId_ErrorCapturado() {

        doThrow(new RuntimeException("Error al borrar"))
                .when(usuarioRepository).deleteById(1L);

        String view = usuarioController.borrarPorId(1L);

        assertEquals("error", view);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void borrarTodo() {

        String view = usuarioController.borrarTodo();

        assertEquals("redirect:/usuarios", view);
        verify(usuarioRepository).deleteAll();
    }

    @Test
    void borrarTodo_ErrorCapturado() {

        doThrow(new RuntimeException("Error al borrar"))
                .when(usuarioRepository).deleteAll();

        String view = usuarioController.borrarTodo();

        assertEquals("error", view);
        verify(usuarioRepository).deleteAll();
    }
}