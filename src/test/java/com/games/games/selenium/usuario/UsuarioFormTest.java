package com.games.games.selenium.usuario;

import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsuarioFormTest {
    @Autowired
    private UsuarioRepository usuarioRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        usuarioRepository.deleteAllInBatch();

        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    void verInputsVacios_enCreacion() {

        usuarioRepository.saveAll(List.of(
                Usuario.builder().nombreUsuario("Javi82").password("javito").build(),
                Usuario.builder().nombreUsuario("Pepe78").password("pepete").build()
        ));

        driver.get("http://localhost:8080/usuarios/new");

        var h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Crear usuario", h1.getText());

        var inputNombreUsuario = driver.findElement(By.id("nombreUsuario"));
        assertTrue(inputNombreUsuario.getAttribute("value").isEmpty());

        var inputPassword = driver.findElement(By.id("password"));
        assertTrue(inputPassword.getAttribute("value").isEmpty());

        var inputNombre = driver.findElement(By.id("nombre"));
        assertTrue(inputNombre.getAttribute("value").isEmpty());

        var inputDireccion = driver.findElement(By.id("direccion"));
        assertTrue(inputDireccion.getAttribute("value").isEmpty());

        var inputCP = driver.findElement(By.id("CP"));
        assertTrue(inputCP.getAttribute("value").isEmpty());

        var inputDNI = driver.findElement(By.id("DNI"));
        assertTrue(inputDNI.getAttribute("value").isEmpty());

    }
}
