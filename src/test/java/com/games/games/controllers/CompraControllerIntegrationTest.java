package com.games.games.controllers;

import com.games.games.models.Compra;
import com.games.games.repositories.CompraRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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


    @Test
    void encontrarTodasCompras() throws Exception {

        compraRepository.saveAll(List.of(
                Compra.builder().id(1L).idUsuario(1L).idJuego(1L).build(),
                Compra.builder().id(2L).idUsuario(2L).idJuego(2L).build(),
                Compra.builder().id(3L).idUsuario(3L).idJuego(3L).build()
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

        var compra = compraRepository.save(Compra.builder().id(1L).idUsuario(1L).idJuego(1L).build());

        System.out.println("Encuentra todas las compras: " + compraRepository.count());
        System.out.println("Encuentra compra guardada: " + compra.getId());

        mockMvc.perform(get("/compras/" + compra.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-list"))
                .andExpect(model().attributeExists("compras"));

    }

    @Test
    void encontrarPorIdCuandoNoExisteUsuario() throws Exception {

        mockMvc.perform(get("/compras/999"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attributeDoesNotExist("compra"));
    }

    @Test
    void formularioParaCrearCompra() throws Exception {

        compraRepository.saveAll(List.of(
                Compra.builder().id(1L).idUsuario(1L).build(),
                Compra.builder().id(2L).idUsuario(2L).build()
                ));

        mockMvc.perform(get("compras/crear"))
                .andExpect(status().isOk())
                .andExpect(view().name("compra-form"))
                .andExpect(model().attributeExists("compra"))
                .andExpect(model().attribute("compras", allOf(
                        hasProperty("id", nullValue()),
                        hasSize(2)
                )));
    }

    @Test
    void formularioParaEditarCompraSiExiste() throws Exception {

        Compra compra = Compra.builder().id(1L).idUsuario(1L).build();
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

        Compra compra = Compra.builder().id(1L).idUsuario(1L).build();
        compraRepository.save(compra);

        mockMvc.perform
                (post("/compras/crear")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

    }

    @Test
    void guardarCompra_Existente() throws Exception {

        Compra compra = Compra.builder().id(1L).idUsuario(1L).build();
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
        assertEquals(2L, compraGuardada.getIdUsuario());
    }

    @Test
    void borrarCompraPorId() throws Exception{

        mockMvc.perform(get("/compras/borrar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));

    }

    @Test
    void borrarTodasLasCompras() throws Exception{

        mockMvc.perform(get("/compras/borrar/todas"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/compras"));
    }
}
