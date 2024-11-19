package com.games.games.selenium.usuario;

import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsuarioListTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        usuarioRepository.deleteAll();

        driver = new ChromeDriver();
        driver.get("http://localhost:8080/usuarios");
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    void title(){
        String title = driver.getTitle();
        System.out.println(title);
        assertEquals("Lista de usuarios", title);
    }

    @Test
    void h1(){
        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Lista de usuarios", h1.getText());
    }

    @Test
    void botonCrearUsuario(){
        WebElement crearBoton = driver.findElement(By.id("btnCrearUsuario"));
        assertEquals("Crear nuevo usuario", crearBoton.getText());

        crearBoton.click();

        assertEquals("http://localhost:8080/usuarios/new", driver.getCurrentUrl());
    }

    @Test
    void tablaVacia(){
        WebElement mensajeNoProductos = driver.findElement(By.id("usuariosVacio"));
        assertEquals("No hay usuarios.", mensajeNoProductos.getText());

        assertThrows(
                NoSuchElementException.class,
                () -> driver.findElement(By.id("usuarioList"))
        );
    }

    @Test
    void tablaConProductos(){
        usuarioRepository.saveAll(List.of(
                Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(2L).nombreUsuario("Pedro").password("1234").nombre("Pedro").direccion("Calle 2").CP(12334).DNI("19797477M").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(3L).nombreUsuario("Carlos").password("1234").nombre("Carlos").direccion("Calle 3").CP(44147).DNI("13464497M").fechaCreacion(Date.from(Instant.now())).build()
        ));

        assertEquals(3, usuarioRepository.count());

        driver.navigate().refresh();

        WebElement usuarioList = driver.findElement(By.id("usuarioList"));
        assertTrue(usuarioList.isDisplayed());
    }

}
