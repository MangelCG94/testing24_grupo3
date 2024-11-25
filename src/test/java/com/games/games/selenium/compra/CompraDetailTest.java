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
//        Instant fechaCompra = Instant.now();

//        Instant fechaCreacion = Instant.ofEpochSecond(600);

        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombreUsuario("Javi82")
                .password("javito")
                .nombre("Javier García")
                .direccion("Acacias 38")
                .CP(28036)
                .DNI("47282382L")
                // .fechaCreacion(fechaCreacion)
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
//        compra = compraRepository.findById(compra.getId()).orElseThrow();
        assertNotNull(compra.getId());
        assertNotNull(compra.getFechaCompra());
//        Compra compra = compraRepository.save(Compra.builder()
////                .fechaCompra(fechaCompra)
//                .usuario(usuario)
//                .juego(juego)
//                .build());

        driver.get("http://localhost:8080/compras/" + compra.getId());

        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Detalle de compra " + compra.getId(), h1.getText());

        // Obtener la fecha de compra desde la página y eliminar texto adicional
        String fechaCompra = driver.findElement(By.id("compra-fecha")).getText();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        assertEquals(formatter.format(compra.getFechaCompra()), fechaCompra);


        // 2024-11-25T09:25:32.757240Z

//
//        String fechaCompraStr = driver.findElement(By.id("compra-fecha"))
//                .getText()
//                .replace("Fecha de compra: ", "")
//                .trim();
//
//        // Formatear con el formato ISO_DATE_TIME
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
//
//        // Parsear la fecha desde la página
//        ZonedDateTime fechaCompraZoned = ZonedDateTime.parse(fechaCompraStr, formatter);
//
//        // Convertir la fecha a la zona horaria local (Europe/Madrid)
//        ZonedDateTime fechaCompraLocalZoned = fechaCompraZoned.withZoneSameInstant(ZoneId.of("Europe/Madrid"))
//                .withMinute(0)
//                .withSecond(0)
//                .withNano(0);
//
//        // Convertir la fecha de compra a ZonedDateTime y redondear a la hora en la zona horaria local
//        ZonedDateTime fechaCompraRedondeada = ZonedDateTime.ofInstant(fechaCompra, ZoneId.of("Europe/Madrid"))
//                .withMinute(0)
//                .withSecond(0)
//                .withNano(0);
//
//        // Comparar ambas fechas redondeadas (hasta la hora)
//        assertEquals(fechaCompraLocalZoned, fechaCompraRedondeada, "Las fechas deberían coincidir hasta la hora");

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
