package com.games.games.selenium.compra;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CompraFormTest {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        compraRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();
        juegoRepository.deleteAllInBatch();

        usuarioRepository.saveAll(List.of(
                Usuario.builder().nombreUsuario("Javi82").password("javito").build(),
                Usuario.builder().nombreUsuario("Pepe78").password("pepete").build()
        ));

        assertEquals(2, usuarioRepository.count());

        juegoRepository.saveAll(List.of(
                Juego.builder().nombre("The Legend of Zelda").precio(24.90).build(),
                Juego.builder().nombre("Age of Empires").precio(29.90).build()
        ));

        assertEquals(2, juegoRepository.count());

        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    void comprobarInputsVacios_Creacion() {


        driver.get("http://localhost:8080/compras/new");

        var h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Crear compra", h1.getText());

        var inputFechaCompra = driver.findElement(By.id("fechaCompra"));
        assertTrue(inputFechaCompra.getAttribute("value").isEmpty());

        Select usuarioSelect = new Select(driver.findElement(By.id("usuario")));
        assertFalse(usuarioSelect.isMultiple());
        assertEquals(3, usuarioSelect.getOptions().size());
        assertEquals("", usuarioSelect.getOptions().get(0).getText());
        assertEquals("Javi82", usuarioSelect.getOptions().get(1).getText());
        assertEquals("Pepe78", usuarioSelect.getOptions().get(2).getText());

        Select juegoSelect = new Select(driver.findElement(By.id("juego")));
        assertFalse(juegoSelect.isMultiple());
        assertEquals(3, juegoSelect.getOptions().size());
        assertEquals("", juegoSelect.getOptions().get(0).getText());
        assertEquals("The Legend of Zelda", juegoSelect.getOptions().get(1).getText());
        assertEquals("Age of Empires", juegoSelect.getOptions().get(2).getText());

    }
    @Test
    void verQueEnCampoCreadoHayCamposLlenos() {

        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombreUsuario("Javi82")
                .password("javito")
                .nombre("Javier García")
                .direccion("Acacias 38")
                .CP(28036)
                .DNI("47282382L")
                .fechaCreacion(Instant.now())
                .build());

        usuarioRepository.save(usuario);

        Juego juego = juegoRepository.save(Juego.builder()
                .nombre("The Legend of Zelda")
                .descripcion("Juego RPG")
                .precio(29.95)
                .videoUrl("URL Zelda")
                .fechaLanzamiento(LocalDate.now())
                .build());

        juegoRepository.save(juego);

        Compra compra = compraRepository.save(Compra.builder()
                .fechaCompra(Instant.now())
                .usuario(usuario)
                .juego(juego)
                .build());

        compraRepository.save(compra);

        driver.get("http://localhost:8080/compras/update/" + compra.getId());

        Select usuarioSelect = new Select(driver.findElement(By.id("usuario")));
        assertFalse(usuarioSelect.isMultiple());
        assertEquals(4,usuarioSelect.getOptions().size());
        assertEquals(String.valueOf(usuario.getId()), usuarioSelect.getFirstSelectedOption().getAttribute("value"));
        assertEquals(usuario.getNombreUsuario(), usuarioSelect.getFirstSelectedOption().getText());

        Select juegoSelect = new Select(driver.findElement(By.id("juego")));
        assertFalse(juegoSelect.isMultiple());
        assertEquals(4,juegoSelect.getOptions().size());
        assertEquals(String.valueOf(juego.getId()), juegoSelect.getFirstSelectedOption().getAttribute("value"));
        assertEquals(juego.getNombre(), juegoSelect.getFirstSelectedOption().getText());


    }

}
