package com.games.games.controllers;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
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

        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        Juego juego2 = Juego.builder().nombre("Juego 2").descripcion("Descripción 2").videoUrl("Url 2").precio(200d).build();
        Juego juego3 = Juego.builder().nombre("Juego 3").descripcion("Descripción 3").videoUrl("Url 3").precio(300d).build();

        juegoRepository.saveAll(List.of(juego1,juego2,juego3));

        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Date.from(Instant.now())).build();
        Usuario usuario2 = Usuario.builder().nombreUsuario("Jose").password("4321").nombre("José López").direccion("Calle 2").CP(15100).DNI("12345673Y").fechaCreacion(Date.from(Instant.now())).build();
        Usuario usuario3 = Usuario.builder().nombreUsuario("María").password("1221").nombre("María Pérez").direccion("Calle 3").CP(13400).DNI("12345679L").fechaCreacion(Date.from(Instant.now())).build();

        usuarioRepository.saveAll(List.of(usuario1,usuario2,usuario3));

        Compra compra1 = Compra.builder().id(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego1).build();
        Compra compra2 = Compra.builder().id(2L).fechaCompra(Instant.ofEpochSecond(2000000000L)).juego(juego2).build();
        Compra compra3 = Compra.builder().id(3L).fechaCompra(Instant.ofEpochSecond(3000000000L)).juego(juego3).build();

        compraRepository.saveAll(List.of(compra1,compra2,compra3));


        System.out.println("Encuentra todas las compras: " + compraRepository.count());

        mockMvc.perform(get("/compras"))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-list"))
                .andExpect(model().attributeExists("compras"))
                .andExpect(model().attribute("compras", hasSize(3)));
    }

    @Test
    void encontrarPorIdCuandoExisteCompra() throws Exception {

        Juego juego = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();

        juegoRepository.save(juego);

        Usuario usuario = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Date.from(Instant.now())).build();

        usuarioRepository.save(usuario);

        Compra compra = Compra.builder().id(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego).usuario(usuario).build();

        compraRepository.save(compra);

        System.out.println("Encuentra todas las compras: " + compraRepository.count());
        System.out.println("Encuentra compra guardada: " + compra.getId());

        mockMvc.perform(get("/compras/" + compra.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-detail"))
                .andExpect(model().attributeExists("compra"));

    }

    @Test
    void encontrarPorIdCuandoNoExisteCompra() throws Exception {



        mockMvc.perform(get("/compras2/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attributeDoesNotExist("compra"));
    }

    @Test
    void formularioParaCrearCompra() throws Exception {

        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        Juego juego2 = Juego.builder().nombre("Juego 2").descripcion("Descripción 2").videoUrl("Url 2").precio(200d).build();
        Juego juego3 = Juego.builder().nombre("Juego 3").descripcion("Descripción 3").videoUrl("Url 3").precio(300d).build();

        juegoRepository.saveAll(List.of(juego1,juego2,juego3));

        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Date.from(Instant.now())).build();
        Usuario usuario2 = Usuario.builder().nombreUsuario("Jose").password("4321").nombre("José López").direccion("Calle 2").CP(15100).DNI("12345673Y").fechaCreacion(Date.from(Instant.now())).build();
        Usuario usuario3 = Usuario.builder().nombreUsuario("María").password("1221").nombre("María Pérez").direccion("Calle 3").CP(13400).DNI("12345679L").fechaCreacion(Date.from(Instant.now())).build();

        usuarioRepository.saveAll(List.of(usuario1,usuario2,usuario3));

        Compra compra1 = Compra.builder().id(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego1).build();
        Compra compra2 = Compra.builder().id(2L).fechaCompra(Instant.ofEpochSecond(2000000000L)).juego(juego2).build();
        Compra compra3 = Compra.builder().id(3L).fechaCompra(Instant.ofEpochSecond(3000000000L)).juego(juego3).build();

        compraRepository.saveAll(List.of(compra1,compra2,compra3));

        mockMvc.perform(get("/compras/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-form"))
                .andExpect(model().attributeExists("compra", "usuario", "juego"));
    }

    @Test
    void formularioParaEditarCompraSiExiste() throws Exception {

        Compra compra = Compra.builder().id(1L).fechaCompra(Instant.ofEpochSecond(100000000)).usuario(Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Date.from(Instant.now())).build()).juego(Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build()).build();

        compraRepository.save(compra);

        mockMvc.perform(get("/compras/update" + compra.getId()))
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

        Compra compra = Compra.builder().id(1L).fechaCompra(Instant.ofEpochSecond(100000000)).usuario(Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Date.from(Instant.now())).build()).juego(Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build()).build();
        compraRepository.save(compra);

        mockMvc.perform
                (post("/compras/crear")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

    }

    @Test
    void guardarCompra_Existente() throws Exception {

        Compra compra = Compra.builder().id(1L).fechaCompra(Instant.ofEpochSecond(100000)).usuario(Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Date.from(Instant.now())).build()).juego(Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build()).build();
        compraRepository.save(compra);

        mockMvc.perform
                (post("/compras")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(compra.getId()))
                        .param("usuario", "2L")
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

        mockMvc.perform(get("/compras/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

    }


}
