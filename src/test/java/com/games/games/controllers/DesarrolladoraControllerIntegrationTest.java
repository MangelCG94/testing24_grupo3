package com.games.games.controllers;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.games.models.Desarrolladora;
import com.games.games.repositories.DesarrolladoraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DesarrolladoraControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DesarrolladoraRepository desarrolladoraRepository;

//    @BeforeEach
//    void setUp() {
//        desarrolladoraRepository.deleteAll();
//    }

    @Test
    void encontrarTodos() throws Exception{
        desarrolladoraRepository.saveAll(List.of(
                Desarrolladora.builder().nombreCom("Ubisoft").pais("Francia").imagenLogo("logo.png").anyoFundacion(1988).build(),
                Desarrolladora.builder().nombreCom("Nintendo").pais("Japón").imagenLogo("logo.png").anyoFundacion(1889).build(),
                Desarrolladora.builder().nombreCom("Electronic Arts").pais("Estados Unidos").imagenLogo("logo.png").anyoFundacion(1982).build()
        ));

        System.out.println("Encuentra todas las desarrolladoras: " + desarrolladoraRepository.count());

//        Thread.sleep(4000L);
        mockMvc.perform(get("/desarrolladoras"))
                .andExpect(status().isOk())
                .andExpect(view().name("desarrolladora-list"))
                .andExpect(model().attributeExists("desarrolladoras"))
                .andExpect(model().attribute("desarrolladoras", hasSize(3)));
    }

    @Test
    void encontrarPorIdCuandoExisteDesarrolladora() throws Exception{
        Desarrolladora desarrolladora = desarrolladoraRepository.save(Desarrolladora.builder().id(1L).nombreCom("Ubisoft").pais("Francia").imagenLogo("logo.png").anyoFundacion(1988).build());

        System.out.println("Ecuentra todos las desarrolladoras: " + desarrolladoraRepository.count());
        System.out.println("Encuentra desarrolladora guardada: " + desarrolladora.getId());

        mockMvc.perform(get("/desarrolladoras/" + desarrolladora.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("desarrolladora-detail"))
                .andExpect(model().attributeExists("desarrolladora"));
    }

    @Test
    void encontrarPorIdCuandoNoExisteDesarrolladora() throws Exception{
        mockMvc.perform(get("/desarrolladoras/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("mensaje"));
//                .andExpect(model().attributeExists("desarrolladora"));
    }
}