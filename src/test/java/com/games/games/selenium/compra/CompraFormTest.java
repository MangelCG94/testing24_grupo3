package com.games.games.selenium.compra;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
import static org.junit.jupiter.api.Assertions.*;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CompraFormTest {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    WebDriver driver;

    @BeforeEach
    void setUp() {
        compraRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();
        juegoRepository.deleteAllInBatch();

        usuarioRepository.saveAll(List.of(
                Usuario.builder().nombreUsuario("Javi82").password("javito").build(),
                Usuario.builder().nombreUsuario("Pepe78").password("pepete").build()
        ));

        assertEquals(2, usuarioRepository.count());

        juegoRepository.saveAll(List.of(
                Juego.builder().nombre("The Legend of Zelda").precio(24.90).build(),
                Juego.builder().nombre("Age of Empires").precio(29.90).build()
        ));

        assertEquals(2, juegoRepository.count());

        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Comprobar que al crear una compra los campos están vacíos")
    void comprobarInputsVacios_Creacion() {


        driver.get("http://localhost:8080/compras/new");

        var h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Crear compra", h1.getText());

        var inputFechaCompra = driver.findElement(By.id("fechaCompra"));
        assertTrue(inputFechaCompra.getAttribute("value").isEmpty());

        Select usuarioSelect = new Select(driver.findElement(By.id("usuario")));
        assertFalse(usuarioSelect.isMultiple());
        assertEquals(3, usuarioSelect.getOptions().size());
        assertEquals("", usuarioSelect.getOptions().get(0).getText());
        assertEquals("Javi82", usuarioSelect.getOptions().get(1).getText());
        assertEquals("Pepe78", usuarioSelect.getOptions().get(2).getText());

        Select juegoSelect = new Select(driver.findElement(By.id("juego")));
        assertFalse(juegoSelect.isMultiple());
        assertEquals(3, juegoSelect.getOptions().size());
        assertEquals("", juegoSelect.getOptions().get(0).getText());
        assertEquals("The Legend of Zelda", juegoSelect.getOptions().get(1).getText());
        assertEquals("Age of Empires", juegoSelect.getOptions().get(2).getText());

    }

    @Test
    @DisplayName("Comprobar que al crear una compra los campos están llenos")
    void verQueEnCampoCreadoHayCamposLlenos() {

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

        Juego juego = juegoRepository.save(Juego.builder()
                .nombre("The Legend of Zelda")
                .descripcion("Juego RPG")
                .precio(29.95)
                .videoUrl("URL Zelda")
                .fechaLanzamiento(LocalDate.now())
                .build());

        juegoRepository.save(juego);

        Compra compra = compraRepository.save(Compra.builder()
                .fechaCompra(Instant.now())
                .usuario(usuario)
                .juego(juego)
                .build());

        compraRepository.save(compra);

        driver.get("http://localhost:8080/compras/update/" + compra.getId());

        Select usuarioSelect = new Select(driver.findElement(By.id("usuario")));
        assertFalse(usuarioSelect.isMultiple());
        assertEquals(4, usuarioSelect.getOptions().size());
        assertEquals(String.valueOf(usuario.getId()), usuarioSelect.getFirstSelectedOption().getAttribute("value"));
        assertEquals(usuario.getNombreUsuario(), usuarioSelect.getFirstSelectedOption().getText());

        Select juegoSelect = new Select(driver.findElement(By.id("juego")));
        assertFalse(juegoSelect.isMultiple());
        assertEquals(4, juegoSelect.getOptions().size());
        assertEquals(String.valueOf(juego.getId()), juegoSelect.getFirstSelectedOption().getAttribute("value"));
        assertEquals(juego.getNombre(), juegoSelect.getFirstSelectedOption().getText());


    }

    @Test
    @DisplayName("Entrar en el formulario, crear una nueva compra y enviar")
    void crearNuevaCompraYEnviar() {

        usuarioRepository.save(Usuario.builder()
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
                .fechaLanzamiento(LocalDate.now())
                .build());

        driver.get("http://localhost:8080/compras/new");

        var inputFechaCompra = driver.findElement(By.id("fechaCompra"));
        inputFechaCompra.sendKeys(Instant.now().toString());

        Select usuarioSelect = new Select(driver.findElement(By.id("usuario")));
        usuarioSelect.selectByVisibleText("Javi82");

        Select juegoSelect = new Select(driver.findElement(By.id("juego")));
        juegoSelect.selectByVisibleText("The Legend of Zelda");

        driver.findElement(By.id("btnSend")).click();

        assertEquals("http://localhost:8080/compras", driver.getCurrentUrl());

        List<WebElement> tableRows = driver.findElements(By.cssSelector("#compraList tbody tr"));
        assertEquals(1, tableRows.size()); // COMPROBAR QUE SE HA CREADO UNA COMPRA
    }

    @Test
    @DisplayName("Entrar en el formulario, editar una compra existente y enviar")
    void editarCompraYEnviar() {
        var usuarios = usuarioRepository.saveAll(List.of(
                Usuario.builder().nombreUsuario("javi82").build(), // 0
                Usuario.builder().nombreUsuario("pepe78").build() // 1
        ));
        Usuario usuario2 = usuarios.getLast();

        var juegos = juegoRepository.saveAll(List.of(
                Juego.builder().nombre("The Legend of Zelda").build(), // 0
                Juego.builder().nombre("Age of Empires").build() // 1
        ));
        Juego juego2 = juegos.getLast();

        Compra compra = compraRepository.save(Compra.builder()
            .fechaCompra(Instant.now())
            .usuario(usuario2)
            .juego(juego2)
            .build());

        driver.get("http://localhost:8080/compras/update/" + compra.getId());

        var inputFechaCompra = driver.findElement(By.id("fechaCompra"));
        inputFechaCompra.sendKeys(Instant.ofEpochSecond(1000).toString());

        Select usuarioSelect = new Select(driver.findElement(By.id("usuario")));
        usuarioSelect.selectByVisibleText("javi82");

        Select juegoSelect = new Select(driver.findElement(By.id("juego")));
        juegoSelect.selectByVisibleText("The Legend of Zelda");

        driver.findElement(By.id("btnSend")).click();

        assertEquals("http://localhost:8080/compras", driver.getCurrentUrl());

        var compraSaved = compraRepository.findAll().getFirst();
        assertEquals("javi82", compraSaved.getUsuario().getNombreUsuario());
        assertEquals("The Legend of Zelda", compraSaved.getJuego().getNombre());

    }

    @Test
    @DisplayName("Comprobar que read only no deja editar")
    void checkIdReadOnly(){
        Compra compra = compraRepository.save(Compra.builder()
                .fechaCompra(Instant.now())
                .build());

        compraRepository.save(compra);
        driver.get("http://localhost:8080/compras/update/" + compra.getId());

        var inputId = driver.findElement(By.id("id"));
        assertEquals(String.valueOf(compra.getId()), inputId.getAttribute("value"));

        inputId.sendKeys("3");
        assertEquals(String.valueOf(compra.getId()), inputId.getAttribute("value"));

    }

}
