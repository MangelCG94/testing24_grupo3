package com.games.games.selenium.desarrolladora;

import com.games.games.models.Desarrolladora;
import com.games.games.repositories.DesarrolladoraRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DesarrolladoraDetailTest {
    @Autowired
    private DesarrolladoraRepository desarrolladoraRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        desarrolladoraRepository.deleteAllInBatch();
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown(){driver.quit();}

    @Test
    void desarrolladoraExisteConTodosLosDetalles(){
        Desarrolladora desarrolladora = desarrolladoraRepository.save(Desarrolladora.builder()
                .nombreCom("Ubisoft")
                .pais("Francia")
                .imagenLogo("logo.png")
                .anyoFundacion(1988)
                .build());

        driver.get("http://localhost:8080/desarrolladoras/" + desarrolladora.getId());

        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Detalle de Desarrolladora", h1.getText());

        assertEquals("Ubisoft", driver.findElement(By.id("desarrolladora-nombreCom")).getText());
        assertEquals("Francia", driver.findElement(By.id("desarrolladora-pais")).getText());
//        assertEquals("logo.png", driver.findElement(By.id("desarrolladora-imagenLogo")).getText());
        assertEquals("1988", driver.findElement(By.id("desarrolladora-anyoFundacion")).getText());
    }

    @Test
    void comprobarBotones(){
        Desarrolladora desarrolladora = desarrolladoraRepository.save(Desarrolladora.builder()
                .nombreCom("Ubisoft")
                .pais("Francia")
                .imagenLogo("logo.png")
                .anyoFundacion(1988)
                .build());

        driver.get("http://localhost:8080/desarrolladoras/" + desarrolladora.getId());

        var backBtn = driver.findElement(By.id("backButton"));
        assertEquals("Volver a la lista", backBtn.getText());
        assertEquals(
                "http://localhost:8080/desarrolladoras",
                backBtn.getAttribute("href")
        );
        backBtn.click();
        assertEquals(
                "http://localhost:8080/desarrolladoras",
                driver.getCurrentUrl()
        );
        driver.navigate().back();

    }

    @Test
    void comprobarDesarrolladoraSiNoExiste(){
        driver.get("http://localhost:8080/desarrolladoras/999");

        assertEquals("Detalle de Desarrolladora", driver.findElement(By.tagName("h1")).getText());
        assertEquals("Desarrolladora no encontrada.", driver.findElement(By.id("desarrolladoraVacia")).getText());
    }
}
