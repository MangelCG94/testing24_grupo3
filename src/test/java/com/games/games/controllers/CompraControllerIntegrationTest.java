package com.games.games.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Al ser integración la base de datos genera los ids por tanto no hace falta asignarlos manualmente
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
    @DisplayName("Encontrar todas las compras")
    void encontrarTodasCompras() throws Exception {

        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        Juego juego2 = Juego.builder().nombre("Juego 2").descripcion("Descripción 2").videoUrl("Url 2").precio(200d).build();
        Juego juego3 = Juego.builder().nombre("Juego 3").descripcion("Descripción 3").videoUrl("Url 3").precio(300d).build();

        juegoRepository.saveAll(List.of(juego1,juego2,juego3));

        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        Usuario usuario2 = Usuario.builder().nombreUsuario("Jose").password("4321").nombre("José López").direccion("Calle 2").CP(15100).DNI("12345673Y").fechaCreacion(Instant.now()).build();
        Usuario usuario3 = Usuario.builder().nombreUsuario("María").password("1221").nombre("María Pérez").direccion("Calle 3").CP(13400).DNI("12345679L").fechaCreacion(Instant.now()).build();

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
    @DisplayName("Encontrar una compra por ID si es que éste existe")
    void encontrarPorIdCuandoExisteCompra() throws Exception {

        Juego juego = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();

        juegoRepository.save(juego);

        Usuario usuario = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();

        usuarioRepository.save(usuario);

        Compra compra = Compra.builder().fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego).usuario(usuario).build();

        compraRepository.save(compra);

        System.out.println("Encuentra todas las compras: " + compraRepository.count());
        System.out.println("Encuentra compra guardada: " + compra.getId());

        mockMvc.perform(get("/compras/" + compra.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-detail"))
                .andExpect(model().attributeExists("compra"));

    }

    @Test
    @DisplayName("Comprobar que al no haber un ID no existe compra")
    void encontrarPorIdCuandoNoExisteCompra() throws Exception {



        mockMvc.perform(get("/compras2/9999"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attributeDoesNotExist("compra"));
    }

    @Test
    @DisplayName("Crear una compra")
    void formularioParaCrearCompra() throws Exception {

        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        Juego juego2 = Juego.builder().nombre("Juego 2").descripcion("Descripción 2").videoUrl("Url 2").precio(200d).build();
        Juego juego3 = Juego.builder().nombre("Juego 3").descripcion("Descripción 3").videoUrl("Url 3").precio(300d).build();

        juegoRepository.saveAll(List.of(juego1,juego2,juego3));

        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        Usuario usuario2 = Usuario.builder().nombreUsuario("Jose").password("4321").nombre("José López").direccion("Calle 2").CP(15100).DNI("12345673Y").fechaCreacion(Instant.now()).build();
        Usuario usuario3 = Usuario.builder().nombreUsuario("María").password("1221").nombre("María Pérez").direccion("Calle 3").CP(13400).DNI("12345679L").fechaCreacion(Instant.now()).build();

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
    @DisplayName("Editar compra si es que su ID existe")
    void formularioParaEditarCompraSiExiste() throws Exception {

        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        usuarioRepository.save(usuario1);
        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        juegoRepository.save(juego1);
        Compra compra = Compra.builder().fechaCompra(Instant.ofEpochSecond(100000000)).usuario(usuario1).juego(juego1).build();
        compraRepository.save(compra);

        mockMvc.perform(get("/compras/update/" + compra.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-form"))
                .andExpect(model().attributeExists("compra", "juegos", "usuarios"));

    }


    @Test
    @DisplayName("Guardar una compra nueva")
    void guardarCompra_Nueva() throws Exception {

        // Crear juegos y usuarios de manera independiente
        Juego juego1 = juegoRepository.save(Juego.builder()
                .id(1L)
                .nombre("Juego 1")
                .descripcion("Descripción 1")
                .videoUrl("Url 1")
                .precio(100d)
                .fechaLanzamiento(LocalDate.now())
                .build());
        Usuario usuario1 = usuarioRepository.save(Usuario.builder()
                .id(1L)
                .nombreUsuario("Juan")
                .password("1234")
                .nombre("Juan Pérez")
                .direccion("Calle 1")
                .CP(15300)
                .DNI("12345678M")
                .fechaCreacion(Instant.now())
                .build());

        // Crear la compra con una fecha fija
        Instant fechaCompra = Instant.now();
        Compra compra1 = Compra.builder().fechaCompra(fechaCompra).usuario(usuario1).juego(juego1).build();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        String fechaCompraStr = formatter.format(fechaCompra);

        // Realizar la solicitud HTTP para guardar la compra a través de mockMvc
        mockMvc.perform(post("/compras")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fechaCompra", fechaCompra.toString()) // Usar la fecha fija
                        .param("juego", String.valueOf(juego1.getId()))
                        .param("usuario", String.valueOf(usuario1.getId()))
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

        // Verificar que la compra se ha guardado correctamente
        List<Compra> compras = compraRepository.findAll();
        assertEquals(1, compras.size()); // Solo debe haber una compra

        // Formatear la fecha guardada de la misma manera
        Instant fechaCompraGuardada = compras.get(0).getFechaCompra();

        // Comprobar que la fecha de la compra es la misma que la que se pasó
        assertEquals(fechaCompra.truncatedTo(ChronoUnit.MINUTES), fechaCompraGuardada.truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    @DisplayName("Modificar una compra y guardarla")
    void guardarCompra_Existente() throws Exception {

        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        juegoRepository.save(juego1);
        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        usuarioRepository.save(usuario1);

        Compra compra = Compra.builder().fechaCompra(Instant.ofEpochSecond(100000000)).usuario(usuario1).juego(juego1).build();
        compraRepository.save(compra);



        mockMvc.perform
                        (post("/compras")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", String.valueOf(compra.getId()))
                                .param("fechaCompra", "2023-01-01T12:34:56Z")
                                .param("juego", String.valueOf(juego1.getId()))
                                .param("usuario", String.valueOf(usuario1.getId()))
                        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

        Optional<Compra> compraOpcionalGuardada = compraRepository.findById(compra.getId());
        assertTrue(compraOpcionalGuardada.isPresent());

        Compra compraGuardada = compraOpcionalGuardada.get();

        assertEquals(compra.getId(), compraGuardada.getId());
        assertEquals("2023-01-01T12:34:56Z", compraGuardada.getFechaCompra().toString());
        assertEquals(juego1.getId(), compraGuardada.getJuego().getId());
        assertEquals(usuario1.getId(), compraGuardada.getUsuario().getId());


    }

    @Test
    @DisplayName("Borrar una compra por ID")
    void borrarCompraPorId() throws Exception{

        mockMvc.perform(get("/compras/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

    }


}
