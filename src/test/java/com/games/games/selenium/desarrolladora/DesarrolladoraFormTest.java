package com.games.games.selenium.desarrolladora;

import com.games.games.models.Desarrolladora;
import com.games.games.repositories.DesarrolladoraRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DesarrolladoraFormTest {
    @Autowired
    private DesarrolladoraRepository desarrolladoraRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        desarrolladoraRepository.deleteAllInBatch();

        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    void verInputsVacios_enCreacion(){
        desarrolladoraRepository.saveAll(List.of(
                Desarrolladora.builder().id(1L).nombreCom("Nintendo").pais("Japón").imagenLogo("logo.png").anyoFundacion(1988).build(),
                Desarrolladora.builder().id(1L).nombreCom("Ubisot").pais("Francia").imagenLogo("logo.png").anyoFundacion(1900).build()
        ));

        driver.get("http://localhost:8080/desarrolladoras/new");

        var h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Crear nueva desarrolladora", h1.getText());

        var inputNombreCom = driver.findElement(By.id("nombreCom"));
        assertTrue(inputNombreCom.getAttribute("value").isEmpty());

        var inputPais = driver.findElement(By.id("pais"));
        assertTrue(inputPais.getAttribute("value").isEmpty());

        var inputImagenLogo = driver.findElement(By.id("imagenLogo"));
        assertTrue(inputImagenLogo.getAttribute("value").isEmpty());

        var inputAnyoFundacion = driver.findElement(By.id("anyoFundacion"));
        assertTrue(inputAnyoFundacion.getAttribute("value").isEmpty());
    }

    @Test
    void verQueEnCampoCreadoHayCamposLlenos(){
        Desarrolladora desarrolladora = Desarrolladora.builder().id(1L).nombreCom("Nintendo").pais("Japón").imagenLogo("logo.png").anyoFundacion(1889).build();

        desarrolladoraRepository.save(desarrolladora);

        driver.get("http://localhost:8080/desarrolladoras/update/" + desarrolladora.getId());

        var inputNombreCom = driver.findElement(By.id("nombreCom"));
        assertEquals("Nintendo", inputNombreCom.getAttribute("value"));

        var inputPais = driver.findElement(By.id("pais"));
        assertEquals("Japón", inputPais.getAttribute("value"));

        var inputImagenLogo = driver.findElement(By.id("imagenLogo"));
        assertEquals("logo.png", inputImagenLogo.getAttribute("value"));

        var inputAnyoFundacion = driver.findElement(By.id("anyoFundacion"));
        assertEquals("1889", inputAnyoFundacion.getAttribute("value"));
    }

    @Test
    void crearNuevaDesarrolladoraYEnviar(){
        driver.get("http://localhost:8080/desarrolladoras/new");

        var inputNombreCom = driver.findElement(By.id("nombreCom"));
        inputNombreCom.sendKeys("Nintendo");

        var inputPais = driver.findElement(By.id("pais"));
        inputPais.sendKeys("Japón");

        var inputImagenLogo = driver.findElement(By.id("imagenLogo"));
        inputImagenLogo.sendKeys("Nintendo");

        var inputAnyoFundacion = driver.findElement(By.id("anyoFundacion"));
        inputAnyoFundacion.sendKeys("1889");

        driver.findElement(By.id("btnSend")).click();

        //Redirigir a la lista de desarrolladoras
        new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.urlToBe("http://localhost:8080/desarrolladoras"));

        assertEquals("http://localhost:8080/desarrolladoras", driver.getCurrentUrl());

        var desarrolladoraSaved = desarrolladoraRepository.findAll().getFirst();
        assertEquals("Nintendo", desarrolladoraSaved.getNombreCom());
        assertEquals("Japón", desarrolladoraSaved.getPais());
        assertEquals("logo.png", desarrolladoraSaved.getImagenLogo());
        assertEquals("1889", desarrolladoraSaved.getAnyoFundacion());
    }
}
