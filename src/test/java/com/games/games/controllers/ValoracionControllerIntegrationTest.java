package com.games.games.controllers;

import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import com.games.games.models.Valoracion;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
import com.games.games.repositories.ValoracionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ValoracionControllerIntegrationTest {
@Autowired
private MockMvc mockMvc;
@Autowired
    private ValoracionRepository valoracionRepository;
@Autowired
    private UsuarioRepository usuarioRepository;
@Autowired
    private JuegoRepository juegoRepository;
@Test
void findAll() throws Exception{
    Usuario usuario1 = Usuario.builder()
            .nombreUsuario("usuario1")
            .password("password1")
            .nombre("Nombre1")
            .direccion("Direccion1")
            .CP(12345)
            .DNI("DNI1")
            .fechaCreacion(new Date().toInstant())
            .build();
    Usuario usuario2 = Usuario.builder()
            .nombreUsuario("usuario2")
            .password("password2")
            .nombre("Nombre2")
            .direccion("Direccion2")
            .CP(72800)
            .DNI("DNI2")
            .fechaCreacion(new Date().toInstant())
            .build();
    usuarioRepository.saveAll(List.of(usuario1,usuario2));
    Juego juego1 = Juego.builder()
            .nombre("juego1")
            .descripcion("descripcion1")
            .videoUrl("www.video1.com")
            .precio(38.45)
            .fechaLanzamiento(new Date())
            .build();
    Juego juego2 = Juego.builder()
            .nombre("juego2")
            .descripcion("descripcion2")
            .videoUrl("www.video2.com")
            .precio(47.45)
            .fechaLanzamiento(new Date())
            .build();
    juegoRepository.saveAll(List.of(juego1, juego2));
    Valoracion valoracion1 = Valoracion.builder()
            .fechaValoracion(20230401)
            .usuario(usuario1)
            .juego(juego1)
            .build();
    Valoracion valoracion2 = Valoracion.builder()
            .fechaValoracion(20230401)
            .usuario(usuario2)
            .juego(juego2)
            .build();
    valoracionRepository.saveAll(List.of(valoracion1, valoracion2));
    mockMvc.perform(get("/valoraciones"))
            .andExpect(status().isOk())
            .andExpect(view().name("valoracion-list"))
            .andExpect(model().attributeExists("valoraciones"))
            .andExpect(model().attribute("valoraciones", hasSize(2)));

}
}
