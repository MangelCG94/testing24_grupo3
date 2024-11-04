package com.games.games.controllers;

import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void encontrarTodos() throws Exception {

        usuarioRepository.saveAll(List.of(
                Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan").direccion("Calle 1").CP(12334).DNI("19797477M").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(2L).nombreUsuario("Pedro").password("2341").nombre("Pedro").direccion("Calle 2").CP(12344).DNI("19237477M").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(3L).nombreUsuario("Carlos").password("3124").nombre("Carlos").direccion("Calle 3").CP(44147).DNI("13464497M").fechaCreacion(Date.from(Instant.now())).build()
        ));

        System.out.println("Encuentra todos los usuarios: " + usuarioRepository.count());

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario-list"))
                .andExpect(model().attributeExists("usuarios"))
                .andExpect(model().attribute("usuarios", hasSize(3)));
    }

    @Test
    void encontrarPorIdCuandoExisteUsuario() throws Exception {

        var usuario = usuarioRepository.save(Usuario.builder().nombreUsuario("Juan").password("1234").build());

        System.out.println("Encuentra todos los usuarios: " + usuarioRepository.count());
        System.out.println("Encuentra usuario guardado: " + usuario.getId());

        mockMvc.perform(get("/usuarios/" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario-list"))
                .andExpect(model().attributeExists("usuarios"));

    }

    @Test
    void encontrarPorIdCuandoNoExisteUsuario() throws Exception {

        mockMvc.perform(get("/usuarios/999"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attributeDoesNotExist("usuario"));
    }

    @Test
    void formularioParaCrearUsuario() throws Exception {

        usuarioRepository.saveAll(List.of(
                Usuario.builder().nombre("Juan").build(),
                Usuario.builder().nombre("José").build()
                ));

        mockMvc.perform(get("usuarios/crear"))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario-form"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attribute("usuario", allOf(
                        hasProperty("id", nullValue()),
                        hasSize(2)
                )));
    }

    @Test
    void formularioParaEditarUsuarioSiExiste() throws Exception {

        Usuario usuario = Usuario.builder()
                .nombreUsuario("Juan").build();
        usuarioRepository.save(usuario);

        mockMvc.perform(get("usuarios/editar/" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario-form"))
                .andExpect(model().attributeExists("usuario"));

    }

    @Test
    void formularioParaEditarUsuario_SiNoExiste() throws Exception {

        mockMvc.perform(get("/usuarios/999"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attributeDoesNotExist("usuario"));
    }

    @Test
    void guardarUsuario_Nuevo() throws Exception {

        Usuario usuario = Usuario.builder().nombreUsuario("Juan").build();
        usuarioRepository.save(usuario);

        mockMvc.perform
                (post("/usuarios/crear")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"));

    }

    @Test
    void guardarUsuario_Existente() throws Exception {

        Usuario usuario = Usuario.builder().nombreUsuario("Juan").build();
        usuarioRepository.save(usuario);

        mockMvc.perform
                (post("/usuarios")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(usuario.getId()))
                        .param("nombreUsuario", "Juan Raimundo")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"));

        Optional<Usuario> OpcionalDeUsuarioGuardado = usuarioRepository.findById(usuario.getId());
        assertTrue(OpcionalDeUsuarioGuardado.isPresent());

        Usuario usuarioGuardado = OpcionalDeUsuarioGuardado.get();

        assertEquals(usuario.getId(), usuarioGuardado.getId());
        assertEquals("Juan Raimundo", usuarioGuardado.getNombreUsuario());
    }

    @Test
    void borrarUsuarioPorId() throws Exception{

        mockMvc.perform(get("/usuarios/borrar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"));

    }

    @Test
    void borrarTodosLosUsuarios() throws Exception{

        mockMvc.perform(get("/usuarios/borrar/todos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"));
    }
}