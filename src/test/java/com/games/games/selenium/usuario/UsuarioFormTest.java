package com.games.games.selenium.usuario;

import com.games.games.models.Compra;
import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsuarioFormTest {
    @Autowired
    private UsuarioRepository usuarioRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        usuarioRepository.deleteAllInBatch();

        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    @DisplayName("Comprobar que al crear un usuario los campos están vacíos")
    void verInputsVacios_enCreacion() {

        usuarioRepository.saveAll(List.of(
                Usuario.builder().nombreUsuario("Javi82").password("javito").build(),
                Usuario.builder().nombreUsuario("Pepe78").password("pepete").build()
        ));

        driver.get("http://localhost:8080/usuarios/new");

        var h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Crear usuario", h1.getText());

        var inputNombreUsuario = driver.findElement(By.id("nombreUsuario"));
        assertTrue(inputNombreUsuario.getAttribute("value").isEmpty());

        var inputPassword = driver.findElement(By.id("password"));
        assertTrue(inputPassword.getAttribute("value").isEmpty());

        var inputNombre = driver.findElement(By.id("nombre"));
        assertTrue(inputNombre.getAttribute("value").isEmpty());

        var inputDireccion = driver.findElement(By.id("direccion"));
        assertTrue(inputDireccion.getAttribute("value").isEmpty());

        var inputCP = driver.findElement(By.id("CP"));
        assertTrue(inputCP.getAttribute("value").isEmpty());

        var inputDNI = driver.findElement(By.id("dni"));
        assertTrue(inputDNI.getAttribute("value").isEmpty());

    }

    @Test
    @DisplayName("Comprobar que al crear un usuario los campos están llenos")
    void verQueEnCampoCreadoHayCamposLlenos(){
        Usuario usuario = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan López").direccion("Gran Vía, 7").CP(28100).DNI("12345678D").build();

        usuarioRepository.save(usuario);

        driver.get("http://localhost:8080/usuarios/update/" + usuario.getId());

        var inputNombreUsuario = driver.findElement(By.id("nombreUsuario"));
        assertEquals("Juan", inputNombreUsuario.getAttribute("value"));

        var inputPassword = driver.findElement(By.id("password"));
        assertEquals("1234",inputPassword.getAttribute("value"));

        var inputNombre = driver.findElement(By.id("nombre"));
        assertEquals("Juan López", inputNombre.getAttribute("value"));

        var inputDireccion = driver.findElement(By.id("direccion"));
        assertEquals("Gran Vía, 7", inputDireccion.getAttribute("value"));

        var inputCP = driver.findElement(By.id("CP"));
        assertEquals("28100", inputCP.getAttribute("value"));

        var inputDNI = driver.findElement(By.id("dni"));
        assertEquals("12345678D", inputDNI.getAttribute("value"));

    }

    @Test
    @DisplayName("Entrar en el formulario, crear un nuevo producto y enviar")
    void crearNuevoProductoYEnviar() {

        driver.get("http://localhost:8080/usuarios/new");



        var inputNombreUsuario = driver.findElement(By.id("nombreUsuario"));
        inputNombreUsuario.sendKeys("Juan");

        var inputPassword = driver.findElement(By.id("password"));
        inputPassword.sendKeys("1234");

        var inputNombre = driver.findElement(By.id("nombre"));
        inputNombre.sendKeys("Juan López");

        var inputDireccion = driver.findElement(By.id("direccion"));
        inputDireccion.sendKeys("Gran Vía, 7");

        var inputCP = driver.findElement(By.id("CP"));
        inputCP.sendKeys("28100");

        var inputDNI = driver.findElement(By.id("dni"));
        inputDNI.sendKeys("12345678D");

        var inputFechaCreacion = driver.findElement(By.id("fechaCreacion"));
        inputFechaCreacion.sendKeys(Instant.now().toString());

        driver.findElement(By.id("btnSend")).click();

// Esperar la redirección a la página de usuarios
        new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.urlToBe("http://localhost:8080/usuarios"));

        assertEquals("http://localhost:8080/usuarios", driver.getCurrentUrl());

        List<WebElement> tableRows = driver.findElements(By.cssSelector("#usuarioList tbody tr"));
        assertEquals(1, tableRows.size()); // comprobar que se ha creado un usuario

        var usuarioSaved = usuarioRepository.findAll().getFirst();
        assertEquals("Juan", usuarioSaved.getNombreUsuario());
        assertEquals("1234", usuarioSaved.getPassword());
        assertEquals("Juan López", usuarioSaved.getNombre());
        assertEquals("Gran Vía, 7", usuarioSaved.getDireccion());
        assertEquals(28100, usuarioSaved.getCP());
        assertEquals("12345678D", usuarioSaved.getDNI());
    }
    @Test
    @DisplayName("Entrar en el formulario, editar un usuario existente y enviar")
    void editarProductYEnviar(){
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombreUsuario("Javi82")
                .password("javito")
                .nombre("Javier García")
                .direccion("Acacias 38")
                .CP(28036)
                .DNI("47282382L")
                .fechaCreacion(Instant.now())
                .build());

        driver.get("http://localhost:8080/usuarios/update/" + usuario.getId());

        var inputNombreUsuario = driver.findElement(By.id("nombreUsuario"));
        inputNombreUsuario.clear();
        inputNombreUsuario.sendKeys("José");

        var inputPassword = driver.findElement(By.id("password"));
        inputPassword.clear();
        inputPassword.sendKeys("5678");

        var inputNombre = driver.findElement(By.id("nombre"));
        inputNombre.clear();
        inputNombre.sendKeys("José García");

        var inputDireccion = driver.findElement(By.id("direccion"));
        inputDireccion.clear();
        inputDireccion.sendKeys("Juan Varela, 63");

        var inputCP = driver.findElement(By.id("CP"));
        inputCP.clear();
        inputCP.sendKeys("15006");

        var inputDNI = driver.findElement(By.id("dni"));
        inputDNI.clear();
        inputDNI.sendKeys("41449774K");

        var inputFechaCreacion = driver.findElement(By.id("fechaCreacion"));
        inputFechaCreacion.sendKeys(Instant.ofEpochSecond(1000).toString());

        WebElement btnSend = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.id("btnSend")));
        btnSend.click();

        assertEquals("http://localhost:8080/usuarios", driver.getCurrentUrl());




    }

    @Test
    @DisplayName("Comprobar que read only no deja editar")
    void checkIdReadOnly(){
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombreUsuario("Javi82")
                .password("javito")
                .nombre("Javier García")
                .direccion("Acacias 38")
                .CP(28036)
                .DNI("47282382L")
                .fechaCreacion(Instant.now())
                .build());

        usuarioRepository.save(usuario);
        driver.get("http://localhost:8080/usuarios/update/" + usuario.getId());

        var inputId = driver.findElement(By.id("id"));
        assertEquals(String.valueOf(usuario.getId()), inputId.getAttribute("value"));

        inputId.sendKeys("3");
        assertEquals(String.valueOf(usuario.getId()), inputId.getAttribute("value"));

    }

}
