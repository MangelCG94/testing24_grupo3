package com.games.games.selenium.juego;

import com.games.games.models.Juego;
import com.games.games.repositories.JuegoRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JuegoListTest {
    @Autowired
    private JuegoRepository juegoRepository;

    WebDriver driver;

    @BeforeEach
    void setUp() {
        juegoRepository.deleteAllInBatch();
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Ver lista de juegos con datos")
    void listaDeJuegosConDatos() {
        Juego juego1 = juegoRepository.save(Juego.builder()
                .nombre("The Legend of Zelda")
                .descripcion("Juego RPG")
                .fechaLanzamiento(LocalDate.of(1986, 2, 21))
                .build());

        Juego juego2 = juegoRepository.save(Juego.builder()
                .nombre("Super Mario Bros")
                .descripcion("Juego de plataformas")
                .fechaLanzamiento(LocalDate.of(1985, 9, 13))
                .build());

        driver.get("http://localhost:8080/juegos");

        List<WebElement> juegos = driver.findElements(By.className("card"));
        assertEquals(2, juegos.size());

        WebElement juegoCard1 = juegos.get(0);
        assertTrue(juegoCard1.getText().contains("The Legend of Zelda"));
        assertTrue(juegoCard1.getText().contains("Juego RPG"));
        assertTrue(juegoCard1.getText().contains("1986-02-21"));

        WebElement juegoCard2 = juegos.get(1);
        assertTrue(juegoCard2.getText().contains("Super Mario Bros"));
        assertTrue(juegoCard2.getText().contains("Juego de plataformas"));
        assertTrue(juegoCard2.getText().contains("1985-09-13"));
    }

    @Test
    @DisplayName("Ver mensaje cuando no hay juegos")
    void mensajeSinJuegos() {
        driver.get("http://localhost:8080/juegos");

        WebElement mensaje = driver.findElement(By.tagName("p"));
        assertEquals("No hay juegos disponibles.", mensaje.getText());
    }

    @Test
    @DisplayName("Probar botones de acciones en la lista de juegos")
    void probarBotonesDeAcciones() {
        Juego juego = juegoRepository.save(Juego.builder()
                .nombre("The Legend of Zelda")
                .descripcion("Juego RPG")
                .fechaLanzamiento(LocalDate.of(1986, 2, 21))
                .build());

        driver.get("http://localhost:8080/juegos");

        WebElement verBtn = driver.findElement(By.cssSelector("a[href='/juegos/" + juego.getId() + "']"));
        assertEquals("Ver", verBtn.getText());
        verBtn.click();
        assertEquals("http://localhost:8080/juegos/" + juego.getId(), driver.getCurrentUrl());
        driver.navigate().back();

        WebElement editarBtn = driver.findElement(By.cssSelector("a[href='/juegos/editar/" + juego.getId() + "']"));
        assertEquals("Editar", editarBtn.getText());
        editarBtn.click();
        assertEquals("http://localhost:8080/juegos/editar/" + juego.getId(), driver.getCurrentUrl());
        driver.navigate().back();

        WebElement borrarBtn = driver.findElement(By.cssSelector("a[href='/juegos/borrar/" + juego.getId() + "']"));
        assertEquals("Borrar", borrarBtn.getText());
        borrarBtn.click();
        assertEquals("http://localhost:8080/juegos", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Crear nuevo juego desde el enlace")
    void crearNuevoJuego() {
        driver.get("http://localhost:8080/juegos");

        WebElement nuevoJuegoBtn = driver.findElement(By.cssSelector("a[href='/juegos/new']"));
        assertEquals("Crear nuevo juego", nuevoJuegoBtn.getText());
        nuevoJuegoBtn.click();
        assertEquals("http://localhost:8080/juegos/new", driver.getCurrentUrl());
    }
}
