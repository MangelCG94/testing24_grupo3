package com.games.games.selenium.usuario;

import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsuarioDetailTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    WebDriver driver;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAllInBatch();
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }


    @Test
    @DisplayName("Ver que un usuario existe y los detalles del mismo")
    void usuarioExisteConTodosLosDetalles() {
        // Crear y guardar usuario con la fecha actual
        Instant fechaActual = Instant.now();
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombreUsuario("Javi82")
                .password("javito")
                .nombre("Javier García")
                .direccion("Acacias 38")
                .CP(28036)
                .DNI("47282382L")
                .fechaCreacion(fechaActual)
                .build());

        driver.get("http://localhost:8080/usuarios/" + usuario.getId());

        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Detalle de usuario " + usuario.getId(), h1.getText());

        assertEquals("Javi82", driver.findElement(By.id("usuario-nickname")).getText());
        assertEquals("javito", driver.findElement(By.id("usuario-password")).getText());
        assertEquals("Javier García", driver.findElement(By.id("usuario-nombre")).getText());
        assertEquals("Acacias 38", driver.findElement(By.id("usuario-direccion")).getText());
        assertEquals("28036", driver.findElement(By.id("usuario-CP")).getText());
        assertEquals("47282382L", driver.findElement(By.id("usuario-DNI")).getText());

        // Obtener la fecha de creación desde la página y eliminar texto adicional
        String fechaCreacionStr = driver.findElement(By.id("usuario-fechaCreacion"))
                .getText()
                .replace("Fecha de creación: ", "")
                .trim();

        // Formatear para parsear con segundos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Parsear la fecha desde la página
        LocalDateTime fechaCreacionLocal = LocalDateTime.parse(fechaCreacionStr, formatter);

        // Convertir la fecha actual a LocalDateTime y redondear a la hora
        LocalDateTime fechaActualLocal = LocalDateTime.ofInstant(fechaActual, ZoneId.systemDefault())
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // Redondear la fecha parseada a la hora
        LocalDateTime fechaCreacionRedondeada = fechaCreacionLocal
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // Comparar ambas fechas redondeadas
        assertEquals(fechaActualLocal, fechaCreacionRedondeada, "Las fechas deberían coincidir hasta la hora");
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

        driver.get("http://localhost:8080/usuarios/" + usuario.getId());

        // edit button
        var editBtn = driver.findElement(By.id("editButton"));
        assertEquals("Editar", editBtn.getText());
        assertEquals(
                "http://localhost:8080/usuarios/update/" + usuario.getId(),
                editBtn.getAttribute("href")
        );
        editBtn.click(); // navega a la pantalla de editar
        assertEquals(
                "http://localhost:8080/usuarios/update/" + usuario.getId(),
                driver.getCurrentUrl()
        );
        driver.navigate().back(); // Volver a la pantalla detalle

        // back button - Probar Volver antes de borrar, ya que si borramos deja de existir
        var backBtn = driver.findElement(By.id("backButton"));
        assertEquals("Volver al listado", backBtn.getText());
        assertEquals(
                "http://localhost:8080/usuarios",
                backBtn.getAttribute("href")
        );
        backBtn.click(); // navega a la pantalla de editar
        assertEquals(
                "http://localhost:8080/usuarios",
                driver.getCurrentUrl()
        );
        driver.navigate().back(); // Volvemos a la pantalla detalle para seguir testeando

        // delete button
        var deleteBtn = driver.findElement(By.id("deleteButton"));
        assertEquals("Borrar", deleteBtn.getText());
        assertEquals(
                "http://localhost:8080/usuarios/delete/" + usuario.getId(),
                deleteBtn.getAttribute("href")
        );
        deleteBtn.click(); // navega a la pantalla de editar
        assertEquals(
                "http://localhost:8080/usuarios",
                driver.getCurrentUrl()
        );

    }

    @Test
    @DisplayName("Ver que un usuario no existe")
    void comprobarUsuarioNoExiste(){
        driver.get("http://localhost:8080/usuarios/999");

        assertEquals("Usuario no encontrado", driver.findElement(By.tagName("h1")).getText());
        assertEquals("No existe el usuario", driver.findElement(By.id("usuarioVacio")).getText());

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("editButton")));
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("deleteButton")));
    }

}
