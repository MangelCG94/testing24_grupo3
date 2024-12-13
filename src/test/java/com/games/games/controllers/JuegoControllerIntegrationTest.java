package com.games.games.controllers;

import com.games.games.models.Desarrolladora;
import com.games.games.models.Juego;
import com.games.games.repositories.DesarrolladoraRepository;
import com.games.games.repositories.JuegoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JuegoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private DesarrolladoraRepository desarrolladoraRepository;

    @BeforeEach
    void setUp() {
        juegoRepository.deleteAll();
        desarrolladoraRepository.deleteAll();
    }

    @Test
    void findAllGames() throws Exception {
        Desarrolladora desarrolladora = Desarrolladora.builder()
                .nombreCom("Desarrolladora A")
                .pais("Estados Unidos")
                .anyoFundacion(2000)
                .build();
        desarrolladoraRepository.save(desarrolladora);

        Juego juego1 = Juego.builder()
                .nombre("Juego 1")
                .descripcion("Descripcion 1")
                .precio(100.0)
                .desarrolladora(desarrolladora)
                .build();

        Juego juego2 = Juego.builder()
                .nombre("Juego 1")
                .descripcion("Descripcion 1")
                .precio(200.0)
                .desarrolladora(desarrolladora)
                .build();

        juegoRepository.saveAll(List.of(juego1, juego2));

        mockMvc.perform(get("/juegos"))
                .andExpect(status().isOk())
                .andExpect(view().name("juego-list"))
                .andExpect(model().attributeExists("juegos"))
                .andExpect(model().attribute("juegos", hasSize(2)));
    }

    @Test
    void findGameById() throws Exception {
        Desarrolladora desarrolladora = Desarrolladora.builder()
                .nombreCom("Desarrolladora A")
                .pais("Estados Unidos")
                .anyoFundacion(2000)
                .build();
        desarrolladoraRepository.save(desarrolladora);

        Juego juego = Juego.builder()
                .nombre("Juego 1")
                .descripcion("Descripcion 1")
                .precio(100.0)
                .desarrolladora(desarrolladora)
                .build();

        juegoRepository.save(juego);

        mockMvc.perform(get("/juegos/" + juego.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("juego-detail"))
                .andExpect(model().attributeExists("juego"));
    }

    @Test
    void createGame() throws Exception {
        Desarrolladora desarrolladora = Desarrolladora.builder()
                .nombreCom("Sonic")
                .pais("Japon")
                .anyoFundacion(2000)
                .build();
        desarrolladoraRepository.save(desarrolladora);

        mockMvc.perform(post("/juegos")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nombre", "Sonic")
                        .param("descripcion", "Descripcion")
                        .param("precio", "100.0")
                        .param("desarrolladora.id", String.valueOf(desarrolladora.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/juegos"));

        List<Juego> juegos = juegoRepository.findAll();
        assertEquals(1, juegos.size());
        assertEquals("Sonic", juegos.get(0).getNombre());
        assertEquals(desarrolladora.getId(), juegos.get(0).getDesarrolladora().getId());
    }

    @Test
    void updateGame() throws Exception {
        Desarrolladora desarrolladora = Desarrolladora.builder()
                .nombreCom("Desarrolladora")
                .pais("Estados Unidos")
                .anyoFundacion(2000)
                .build();
        desarrolladoraRepository.save(desarrolladora);

        Juego juego = Juego.builder()
                .nombre("Juego 1")
                .descripcion("Descripcion 1")
                .precio(100.0)
                .desarrolladora(desarrolladora)
                .build();
        juegoRepository.save(juego);

        mockMvc.perform(post("/juegos")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(juego.getId()))
                        .param("nombre", "Juego actualizado 1")
                        .param("descripcion", "Descripcion actualizada 1")
                        .param("precio", "150.0")
                        .param("desarrolladora.id", String.valueOf(desarrolladora.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/juegos"));

        Optional<Juego> updatedJuego = juegoRepository.findById(juego.getId());
        assertTrue(updatedJuego.isPresent());
        assertEquals("Juego actualizado 1", updatedJuego.get().getNombre());
    }

    @Test
    void deleteGameById() throws Exception {
        Juego juego = Juego.builder()
                .nombre("Mario")
                .descripcion("Juego de plataformas")
                .precio(20.0)
                .build();
        juegoRepository.save(juego);

        mockMvc.perform(get("/juegos/borrar/" + juego.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/juegos"));

        assertEquals(0, juegoRepository.count());
    }
}
