package com.games.games.selenium.compra;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CompraDetailTest {
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
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    @DisplayName("Ver que una compra existe y los detalles de la misma")
    void compraExisteConTodosLosDetalles() {
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombreUsuario("Javi82")
                .password("javito")
                .nombre("Javier García")
                .direccion("Acacias 38")
                .CP(28036)
                .DNI("47282382L")
                .build());

        Juego juego = juegoRepository.save(Juego.builder()
                .nombre("The Legend of Zelda")
                .descripcion("Juego RPG")
                .precio(29.95)
                .videoUrl("URL Zelda")
                .fechaLanzamiento((LocalDate.now()))
                .build());

        Compra compra = new Compra();
        compra.setJuego(juego);
        compra.setUsuario(usuario);
        compraRepository.save(compra);

        assertNotNull(compra.getId());
        assertNotNull(compra.getFechaCompra());


        driver.get("http://localhost:8080/compras/" + compra.getId());

        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Detalle de compra " + compra.getId(), h1.getText());

        // Obtener la fecha de compra desde la página y eliminar texto adicional
        String fechaCompra = driver.findElement(By.id("compra-fecha")).getText();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        assertEquals(formatter.format(compra.getFechaCompra()), fechaCompra);


        WebElement usuarioLink = driver.findElement(By.id("usuarioLink"));
        assertEquals("http://localhost:8080/usuarios/" + usuario.getId(), usuarioLink.getAttribute("href"));
        assertEquals("Javier García", usuarioLink.getText());

        WebElement juegoLink = driver.findElement(By.id("juegoLink"));
        assertEquals("http://localhost:8080/juegos/" + juego.getId(), juegoLink.getAttribute("href"));
        assertEquals("The Legend of Zelda", juegoLink.getText());
    }

    @Test
    @DisplayName("Comprobar que un usuario no existe")
    void comprobarUsuarioNulo(){

        Juego juego = juegoRepository.save(Juego.builder()
                .nombre("The Legend of Zelda")
                .descripcion("Juego RPG")
                .precio(29.95)
                .videoUrl("URL Zelda")
                .fechaLanzamiento(LocalDate.now())
                .build());

        Compra compra = compraRepository.save(Compra.builder()
                .fechaCompra(Instant.now())
                .juego(juego)
                .build());

        driver.get("http://localhost:8080/compras/" + compra.getId());

        WebElement usuarioVacio = driver.findElement(By.id("usuarioVacio"));
        assertEquals("Sin usuario", usuarioVacio.getText());
    }

    @Test
    @DisplayName("Comprobar que el formulario aparece relleno al editar una compra")
    void comprobarCamposLlenosAlEditarCompra() {
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombreUsuario("Javi82")
                .password("javito")
                .nombre("Javier García")
                .direccion("Acacias 38")
                .CP(28036)
                .DNI("47282382L")
                .build());

        Juego juego = juegoRepository.save(Juego.builder()
                .nombre("The Legend of Zelda")
                .descripcion("Juego RPG")
                .precio(29.95)
                .videoUrl("URL Zelda")
                .fechaLanzamiento((LocalDate.now()))
                .build());

        Compra compra = compraRepository.save(Compra.builder()
                .usuario(usuario)
                .juego(juego)
                .build());

        driver.get("http://localhost:8080/compras/update/" + compra.getId());

        Select usuarioSelect = new Select(driver.findElement(By.id("usuario")));
        assertFalse(usuarioSelect.isMultiple());
        assertEquals(2, usuarioSelect.getOptions().size());
        assertEquals(String.valueOf(usuario.getId()), usuarioSelect.getFirstSelectedOption().getAttribute("value"));
        assertEquals("Javi82", usuarioSelect.getFirstSelectedOption().getText());

        Select juegoSelect = new Select(driver.findElement(By.id("juego")));
        assertFalse(juegoSelect.isMultiple());
        assertEquals(2, juegoSelect.getOptions().size());
        assertEquals(String.valueOf(juego.getId()), juegoSelect.getFirstSelectedOption().getAttribute("value"));
        assertEquals(juego.getNombre(), juegoSelect.getFirstSelectedOption().getText());

    }

    @Test
    @DisplayName("Comprobar que funcionan los botones de la aplicación")
    void comprobarBotones(){
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombreUsuario("Javi82")
                .password("javito")
                .nombre("Javier García")
                .direccion("Acacias 38")
                .CP(28036)
                .DNI("47282382L")
                .fechaCreacion(Instant.now())
                .build());

        Juego juego = juegoRepository.save(Juego.builder()
                .nombre("The Legend of Zelda")
                .descripcion("Juego RPG")
                .precio(29.95)
                .videoUrl("URL Zelda")
                .fechaLanzamiento((LocalDate.now()))
                .build());

        Compra compra = compraRepository.save(Compra.builder()
                .fechaCompra(Instant.now())
                .usuario(usuario)
                .juego(juego)
                .build());

        driver.get("http://localhost:8080/compras/" + compra.getId());

        // edit button
        var editBtn = driver.findElement(By.id("editButton"));
        assertEquals("Editar", editBtn.getText());
        assertEquals(
                "http://localhost:8080/compras/update/" + compra.getId(),
                editBtn.getAttribute("href")
        );
        editBtn.click(); // navega a la pantalla de editar
        assertEquals(
                "http://localhost:8080/compras/update/" + compra.getId(),
                driver.getCurrentUrl()
        );
        driver.navigate().back(); // Volver a la pantalla detalle

        // back button - Probar Volver antes de borrar, ya que si borramos deja de existir
        var backBtn = driver.findElement(By.id("backButton"));
        assertEquals("Volver a la lista", backBtn.getText());
        assertEquals(
                "http://localhost:8080/compras",
                backBtn.getAttribute("href")
        );
        backBtn.click(); // navega a la pantalla de editar
        assertEquals(
                "http://localhost:8080/compras",
                driver.getCurrentUrl()
        );
        driver.navigate().back(); // Volvemos a la pantalla detalle para seguir testeando

        // delete button
        var deleteBtn = driver.findElement(By.id("deleteButton"));
        assertEquals("Borrar", deleteBtn.getText());
        assertEquals(
                "http://localhost:8080/compras/delete/" + compra.getId(),
                deleteBtn.getAttribute("href")
        );
        deleteBtn.click(); // navega a la pantalla de editar
        assertEquals(
                "http://localhost:8080/compras",
                driver.getCurrentUrl()
        );

    }

    @Test
    @DisplayName("Comprobar que una compra no existe")
    void comprobarCompraNoExiste(){

        driver.get("http://localhost:8080/compras/999");

        assertEquals("Compra no encontrada", driver.findElement(By.tagName("h1")).getText());
        assertEquals("No existe la compra", driver.findElement(By.id("compraVacia")).getText());

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("editButton")));
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("deleteButton")));
    }
}
