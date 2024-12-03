package com.games.games.selenium.compra;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CompraListTest {
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
        driver.get("http://localhost:8080/compras");
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    @DisplayName("Comprobar el título de la página")
    void title(){
        String title = driver.getTitle();
        System.out.println(title);
        assertEquals("Lista de compras", title);
    }

    @Test
    @DisplayName("Ver los elementos h1 (los más grandes a la vista) de la página")
    void h1(){
        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Lista de compras", h1.getText());
    }

    @Test
    @DisplayName("Comprobar que funciona el botón de crear compra")
    void botonCrearCompra(){
        WebElement crearBoton = driver.findElement(By.id("btnCrearCompra"));
        assertEquals("Crear nueva compra", crearBoton.getText());

        crearBoton.click();

        assertEquals("http://localhost:8080/compras/new", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Comprobar que una tabla de compras está vacía")
    void tablaVacia(){
        WebElement mensajeNoCompras = driver.findElement(By.id("compraVacia"));
        assertEquals("No hay compras.", mensajeNoCompras.getText());

        assertThrows(
                NoSuchElementException.class,
                () -> driver.findElement(By.id("compraList"))
        );
    }

    @Test
    @DisplayName("Comprobar que una tabla tiene compras")
    void tablaConCompras(){
        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        Juego juego2 = Juego.builder().nombre("Juego 2").descripcion("Descripción 2").videoUrl("Url 2").precio(200d).build();
        Juego juego3 = Juego.builder().nombre("Juego 3").descripcion("Descripción 3").videoUrl("Url 3").precio(300d).build();

        juegoRepository.saveAll(List.of(juego1,juego2,juego3));

        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        Usuario usuario2 = Usuario.builder().nombreUsuario("Jose").password("4321").nombre("José López").direccion("Calle 2").CP(15100).DNI("12345673Y").fechaCreacion(Instant.now()).build();
        Usuario usuario3 = Usuario.builder().nombreUsuario("María").password("1221").nombre("María Pérez").direccion("Calle 3").CP(13400).DNI("12345679L").fechaCreacion(Instant.now()).build();

        usuarioRepository.saveAll(List.of(usuario1,usuario2,usuario3));

        Compra compra1 = Compra.builder().id(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego1).build();
        Compra compra2 = Compra.builder().id(2L).fechaCompra(Instant.ofEpochSecond(2000000000L)).juego(juego2).build();
        Compra compra3 = Compra.builder().id(3L).fechaCompra(Instant.ofEpochSecond(3000000000L)).juego(juego3).build();

        compraRepository.saveAll(List.of(compra1,compra2,compra3));

        assertEquals(3, compraRepository.count());

        driver.navigate().refresh();

        WebElement compraList = driver.findElement(By.id("compraList"));
        assertTrue(compraList.isDisplayed());
    }

    @Test
    @DisplayName("Comprobar las columnas y los datos de una tabla de compras")
    void tablaConCompras_Columnas(){
        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        Juego juego2 = Juego.builder().nombre("Juego 2").descripcion("Descripción 2").videoUrl("Url 2").precio(200d).build();
        Juego juego3 = Juego.builder().nombre("Juego 3").descripcion("Descripción 3").videoUrl("Url 3").precio(300d).build();

        juegoRepository.saveAll(List.of(juego1,juego2,juego3));

        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        Usuario usuario2 = Usuario.builder().nombreUsuario("Jose").password("4321").nombre("José López").direccion("Calle 2").CP(15100).DNI("12345673Y").fechaCreacion(Instant.now()).build();
        Usuario usuario3 = Usuario.builder().nombreUsuario("María").password("1221").nombre("María Pérez").direccion("Calle 3").CP(13400).DNI("12345679L").fechaCreacion(Instant.now()).build();

        usuarioRepository.saveAll(List.of(usuario1,usuario2,usuario3));

        Compra compra1 = Compra.builder().id(1L).fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego1).build();
        Compra compra2 = Compra.builder().id(2L).fechaCompra(Instant.ofEpochSecond(2000000000L)).juego(juego2).build();
        Compra compra3 = Compra.builder().id(3L).fechaCompra(Instant.ofEpochSecond(3000000000L)).juego(juego3).build();

        compraRepository.saveAll(List.of(compra1,compra2,compra3));

        assertEquals(3, compraRepository.count());

        driver.navigate().refresh();

        WebElement compraList = driver.findElement(By.id("compraList"));

        List<WebElement> headers = compraList.findElements(By.tagName("th"));
        assertEquals(4, headers.size());
        assertEquals("ID", headers.get(0).getText());
        assertEquals("FECHA DE COMPRA", headers.get(1).getText());
        assertEquals("ID DE USUARIO", headers.get(2).getText());
        assertEquals("ID DE JUEGO", headers.get(3).getText());
    }

    @Test
    @DisplayName("Comprobar las filas y los datos de una tabla de compras")
    void tablaConCompras_Filas(){
        Juego juego1 = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        Juego juego2 = Juego.builder().nombre("Juego 2").descripcion("Descripción 2").videoUrl("Url 2").precio(200d).build();
        Juego juego3 = Juego.builder().nombre("Juego 3").descripcion("Descripción 3").videoUrl("Url 3").precio(300d).build();

        juegoRepository.saveAll(List.of(juego1,juego2,juego3));

        Usuario usuario1 = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        Usuario usuario2 = Usuario.builder().nombreUsuario("Jose").password("4321").nombre("José López").direccion("Calle 2").CP(15100).DNI("12345673Y").fechaCreacion(Instant.now()).build();
        Usuario usuario3 = Usuario.builder().nombreUsuario("María").password("1221").nombre("María Pérez").direccion("Calle 3").CP(13400).DNI("12345679L").fechaCreacion(Instant.now()).build();

        usuarioRepository.saveAll(List.of(usuario1,usuario2,usuario3));

        Compra compra1 = Compra.builder().id(1L).fechaCompra(Instant.now()).juego(juego1).build();
        Compra compra2 = Compra.builder().id(2L).fechaCompra(Instant.now()).juego(juego2).build();
        Compra compra3 = Compra.builder().id(3L).fechaCompra(Instant.now()).juego(juego3).build();

        compraRepository.saveAll(List.of(compra1,compra2,compra3));

        assertEquals(3, compraRepository.count());

        driver.navigate().refresh();

        WebElement compraList = driver.findElement(By.id("compraList"));
        List<WebElement> filas = compraList.findElements(By.tagName("tr"));
        assertEquals(4, filas.size());

        List<WebElement> filasTabla = driver.findElements(By.cssSelector("#compraList tbody tr"));
        assertEquals(3, filasTabla.size());

        WebElement primeraFila = filasTabla.getFirst();
        var datosPrimeraFila = primeraFila.findElements(By.tagName("td"));

    }
    @Test
    @DisplayName("Comprobar las filas y sus datos con IDs dinámicos")
    void tablaConCompras_Filas_IDs(){
        Juego juego = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        juegoRepository.save(juego);

        Usuario usuario = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        usuarioRepository.save(usuario);

        Instant fechaCompraInstant = Instant.ofEpochSecond(1000000000L);

        Compra compra = Compra.builder().fechaCompra(fechaCompraInstant).juego(juego).usuario(usuario).build();
        compraRepository.save(compra);

        driver.navigate().refresh();

        WebElement id = driver.findElement(By.id("compraId_" + compra.getId()));
        assertEquals(compra.getId(), Long.valueOf(id.getText()));

        WebElement idUsuario = driver.findElement(By.id("compraIdUsuario_" + compra.getId()));
        assertEquals(String.valueOf(usuario.getId()), idUsuario.getText());

        WebElement idJuego = driver.findElement(By.id("compraIdJuego_" + compra.getId()));
        assertEquals(juego.getId(), Long.valueOf(idJuego.getText()));

    }

    @Test
    @DisplayName("Comprobar que funciona el botón de ver compra")
    void tablaConCompras_accionBotonVer() {
        Juego juego = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        juegoRepository.save(juego);

        Usuario usuario = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        usuarioRepository.save(usuario);

        Compra compra = Compra.builder().fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego).usuario(usuario).build();
        compraRepository.save(compra);

        driver.navigate().refresh();

        var botonVer = driver.findElement(By.id("compraActionView_" + compra.getId()));
        assertEquals("Ver", botonVer.getText());

        botonVer.click();

        assertEquals("http://localhost:8080/compras/" + compra.getId(), driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Comprobar que funciona el botón de editar compra")
    void tablaConCompras_accionBotonEditar() {
        Juego juego = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        juegoRepository.save(juego);

        Usuario usuario = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        usuarioRepository.save(usuario);

        Compra compra = Compra.builder().fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego).usuario(usuario).build();
        compraRepository.save(compra);

        driver.navigate().refresh();

        var botonEditar = driver.findElement(By.id("compraActionEdit_" + compra.getId()));
        assertEquals("Editar", botonEditar.getText());

        botonEditar.click();

        assertEquals("http://localhost:8080/compras/update/" + compra.getId(), driver.getCurrentUrl());
    }


    @Test
    @DisplayName("Comprobar que funciona el botón de borrar compra")
    void tablaConCompras_accionBotonBorrar() {
        Juego juego = Juego.builder().nombre("Juego 1").descripcion("Descripción 1").videoUrl("Url 1").precio(100d).build();
        juegoRepository.save(juego);

        Usuario usuario = Usuario.builder().nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678M").fechaCreacion(Instant.now()).build();
        usuarioRepository.save(usuario);

        Compra compra = Compra.builder().fechaCompra(Instant.ofEpochSecond(1000000000L)).juego(juego).usuario(usuario).build();
        compraRepository.save(compra);

        driver.navigate().refresh();

        var botonBorrar = driver.findElement(By.id("compraActionDelete_" + compra.getId()));
        assertEquals("Borrar", botonBorrar.getText());

        botonBorrar.click();

        assertEquals("http://localhost:8080/compras", driver.getCurrentUrl());

        WebElement mensajeNoCompras = driver.findElement(By.id("compraVacia"));
        assertEquals("No hay compras.", mensajeNoCompras.getText());

        assertThrows(
                NoSuchElementException.class,
                () -> driver.findElement(By.id("compraList"))
        );
    }

}
