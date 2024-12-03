package com.games.games.selenium.layout;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NavbarTest {

    WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/compras");
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Comprobar logo navbar con link a compras")
    void chequearLogoConLink(){
        var logo = driver.findElement(By.cssSelector("a.navbar-brand > img"));
        assertTrue(logo.isDisplayed());

        driver.findElement(By.id("homeLink")).click();
        assertEquals("http://localhost:8080/compras", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Comprobar menú de usuarios")
    void navegarAMenuUsuarios() {
        driver.findElement(By.id("usuariosLink")).click();
        assertEquals("http://localhost:8080/usuarios", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Comprobar menú de juegos")
    void navegarAMenuJuegos() {
        driver.findElement(By.id("juegosLink")).click();
        assertEquals("http://localhost:8080/juegos", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Comprobar menú de juegos de usuario")
    void navegarAMenuDesarolladoras() {
        driver.findElement(By.id("desarrolladorasLink")).click();
        assertEquals("http://localhost:8080/desarrolladoras", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Comprobar la web colapsada en móvil")
    void verBarraNavegacionMovil() {
        driver.manage().window().setSize(new Dimension(380,840));
        System.out.println("fin");
    }

    @Test
    @DisplayName("Comprobar la web colapsada en móvil con espera")
    void verBarraNavegacionMovilConEspera() {
        driver.manage().window().setSize(new Dimension(380,840));
        assertFalse(driver.findElement(By.id("usuariosLink")).isDisplayed());

        driver.findElement(By.cssSelector("button.navbar-toggler")).click();

        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(driver -> driver.findElement(By.id("usuariosLink")).isDisplayed());

        assertTrue(driver.findElement(By.id("usuariosLink")).isDisplayed());

        driver.findElement(By.id("usuariosLink")).click();
        assertEquals("http://localhost:8080/usuarios", driver.getCurrentUrl());
    }
}
