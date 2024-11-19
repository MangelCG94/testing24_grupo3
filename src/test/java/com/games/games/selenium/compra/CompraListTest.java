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
public class CompraListTest {
    @Autowired
    CompraRepository compraRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JuegoRepository juegoRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        compraRepository.deleteAll();
        usuarioRepository.deleteAll();
        juegoRepository.deleteAll();

        driver = new ChromeDriver();
        driver.get("http://localhost:8080/compras");
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    void title(){
        String title = driver.getTitle();
        System.out.println(title);
        assertEquals("Lista de compras", title);
    }

    @Test
    void h1(){
        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Lista de compras", h1.getText());
    }

    @Test
    void botonCrearCompra(){
        WebElement crearBoton = driver.findElement(By.id("btnCrearCompra"));
        assertEquals("Crear nueva compra", crearBoton.getText());

        crearBoton.click();

        assertEquals("http://localhost:8080/compras/new", driver.getCurrentUrl());
    }

    @Test
    void tablaVacia(){
        WebElement mensajeNoCompras = driver.findElement(By.id("compraVacia"));
        assertEquals("No hay compras.", mensajeNoCompras.getText());

        assertThrows(
                NoSuchElementException.class,
                () -> driver.findElement(By.id("compraList"))
        );
    }

    @Test
    void tablaConCompras(){
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

        assertEquals(3, compraRepository.count());

        driver.navigate().refresh();

        WebElement compraList = driver.findElement(By.id("compraList"));
        assertTrue(compraList.isDisplayed());
    }

}
