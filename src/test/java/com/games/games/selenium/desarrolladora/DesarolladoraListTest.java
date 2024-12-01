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

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DesarolladoraListTest {
    @Autowired
    private DesarrolladoraRepository desarrolladoraRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        desarrolladoraRepository.deleteAllInBatch();

        driver = new ChromeDriver();
        driver.get("http://localhost:8080/desarrolladoras");
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    void title(){
        String title = driver.getTitle();
        System.out.println(title);
        assertEquals("Lista de desarrolladoras", title);
    }

    @Test
    void h1(){
        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Listado de desarrolladoras", h1.getText());
    }

    @Test
    void botonCrearDesarrolladora(){
        WebElement crearBtn = driver.findElement(By.id("btnCrearDesarrolladora"));
        assertEquals("Crear nueva desarrolladora", crearBtn.getText());

        crearBtn.click();

        assertEquals("http://localhost:8080/desarrolladoras/new", driver.getCurrentUrl());
    }
}
