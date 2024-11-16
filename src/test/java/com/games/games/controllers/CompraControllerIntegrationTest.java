package com.games.games.controllers;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.JuegosUsuario;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompraControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    @BeforeEach
    void setUp(){
        compraRepository.deleteAll();
        usuarioRepository.deleteAll();
        juegoRepository.deleteAll();

    }

    @Test
    void encontrarTodasCompras() throws Exception {


        usuarioRepository.saveAll(List.of(
                Usuario.builder().id(1L).nombre("Usuario 1").password("Password 1").nombreUsuario("Nombre 1").direccion("Dirección 1").CP(11111).DNI("11111111A").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(1L).nombre("Usuario 2").password("Password 2").nombreUsuario("Nombre 2").direccion("Dirección 2").CP(22222).DNI("22222222A").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(1L).nombre("Usuario 3").password("Password 3").nombreUsuario("Nombre 3").direccion("Dirección 2").CP(33333).DNI("33333333A").fechaCreacion(Date.from(Instant.now())).build()
        ));

        juegoRepository.saveAll(List.of(
                Juego.builder().id(1L).nombre("Juego 1").descripcion("Descripción de juego 1").videoUrl("Url 1").precio(100d).build(),
                Juego.builder().id(2L).nombre("Juego 2").descripcion("Descripción de juego 2").videoUrl("Url 2").precio(100d).build(),
                Juego.builder().id(3L).nombre("Juego 3").descripcion("Descripción de juego 3").videoUrl("Url 3").precio(100d).build()
        ));

        compraRepository.saveAll(List.of(
                Compra.builder().id(1L).usuario(usuarioRepository.findById(1L).orElseThrow()).juego(juegoRepository.findById(1L).orElseThrow()).build(),
                Compra.builder().id(2L).usuario(usuarioRepository.findById(2L).orElseThrow()).juego(juegoRepository.findById(2L).orElseThrow()).build(),
                Compra.builder().id(3L).usuario(usuarioRepository.findById(3L).orElseThrow()).juego(juegoRepository.findById(3L).orElseThrow()).build()
        ));

        System.out.println("Encuentra todas las compras: " + compraRepository.count());

        mockMvc.perform(get("/compras"))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-list"))
                .andExpect(model().attributeExists("compras"))
                .andExpect(model().attribute("compras", hasSize(3)));
    }

    @Test
    void encontrarPorIdCuandoExisteCompra() throws Exception {

        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .id(1L)
                .nombre("Usuario 1")
                .password("Password 1")
                .nombreUsuario("Nombre 1")
                .direccion("Dirección 1")
                .CP(11111)
                .DNI("11111111A")
                .fechaCreacion(Date.from(Instant.now()))
                .build());

        Juego juego = juegoRepository.save(Juego.builder()
                .id(1L)
                .nombre("Juego 1")
                .descripcion("Descripción de juego 1")
                .videoUrl("Url 1")
                .precio(100d)
                .build());

        Compra compra = compraRepository.save(Compra.builder()
                .id(1L)
                .usuario(usuarioRepository
                        .findById(1L)
                        .orElseThrow())
                .juego(juegoRepository
                        .findById(1L)
                        .orElseThrow())
                .build());

        System.out.println("Encuentra todas las compras: " + compraRepository.count());
        System.out.println("Encuentra compra guardada: " + compra.getId());

        mockMvc.perform(get("/compras/" + compra.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-detail"))
                .andExpect(model().attributeExists("compra"));

    }

    @Test
    void encontrarPorIdCuandoNoExisteUsuario() throws Exception {

        mockMvc.perform(get("/compras/9999"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attributeDoesNotExist("compra"));
    }

    @Test
    void formularioParaCrearCompra() throws Exception {


        usuarioRepository.saveAll(List.of(
                Usuario.builder().id(1L).nombre("Usuario 1").password("Password 1").nombreUsuario("Nombre 1").direccion("Dirección 1").CP(11111).DNI("11111111A").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(2L).nombre("Usuario 2").password("Password 2").nombreUsuario("Nombre 2").direccion("Dirección 2").CP(22222).DNI("22222222A").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(3L).nombre("Usuario 3").password("Password 3").nombreUsuario("Nombre 3").direccion("Dirección 3").CP(33333).DNI("33333333A").fechaCreacion(Date.from(Instant.now())).build()
        ));

        juegoRepository.saveAll(List.of(
                Juego.builder().id(1L).nombre("Juego 1").descripcion("Descripción de juego 1").videoUrl("Url 1").precio(100d).build(),
                Juego.builder().id(2L).nombre("Juego 2").descripcion("Descripción de juego 2").videoUrl("Url 2").precio(100d).build(),
                Juego.builder().id(3L).nombre("Juego 3").descripcion("Descripción de juego 3").videoUrl("Url 3").precio(100d).build()
        ));

        compraRepository.saveAll(List.of(
                Compra.builder().usuario(usuarioRepository.findById(1L).orElseThrow()).juego(juegoRepository.findById(1L).orElseThrow()).build(),
                Compra.builder().usuario(usuarioRepository.findById(2L).orElseThrow()).juego(juegoRepository.findById(2L).orElseThrow()).build(),
                Compra.builder().usuario(usuarioRepository.findById(3L).orElseThrow()).juego(juegoRepository.findById(3L).orElseThrow()).build()
        ));

        mockMvc.perform(get("/compras/crear"))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-form"))
                .andExpect(model().attributeExists("compra", "usuario", "juego"));
    }

    @Test
    void formularioParaEditarCompraSiExiste() throws Exception {

        Compra compra = Compra.builder().id(1L).usuario(Usuario.builder().id(1L).build()).juego(Juego.builder().id(1L).build()).build();

        compraRepository.save(compra);

        mockMvc.perform(get("compras/editar/" + compra.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-form"))
                .andExpect(model().attributeExists("compra"));

    }

    @Test
    void formularioParaEditarCompra_SiNoExiste() throws Exception {

        mockMvc.perform(get("/compras/999"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attributeDoesNotExist("compra"));
    }

    @Test
    void guardarCompra_Nueva() throws Exception {

        Compra compra = Compra.builder().id(1L).usuario(Usuario.builder().id(1L).build()).juego(Juego.builder().id(1L).build()).build();
        compraRepository.save(compra);

        mockMvc.perform
                (post("/compras/crear")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

    }

    @Test
    void guardarCompra_Existente() throws Exception {

        Compra compra = Compra.builder().id(1L).usuario(Usuario.builder().id(1L).build()).juego(Juego.builder().id(1L).build()).build();
        compraRepository.save(compra);

        mockMvc.perform
                (post("/compras")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(compra.getId()))
                        .param("idUsuario", "2L")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

        Optional<Compra> OpcionalDeCompraGuardada = compraRepository.findById(compra.getId());
        assertTrue(OpcionalDeCompraGuardada.isPresent());

        Compra compraGuardada = OpcionalDeCompraGuardada.get();

        assertEquals(compra.getId(), compraGuardada.getId());
        assertEquals(2L, compraGuardada.getJuego().getId(), compraGuardada.getUsuario().getId());
    }

    @Test
    void borrarCompraPorId() throws Exception{

        mockMvc.perform(get("/compras/borrar/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

    }


}
