package com.games.games.selenium.usuario;

import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
public class UsuarioListTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    WebDriver driver;

    @BeforeEach
    void setUp(){
        usuarioRepository.deleteAll();

        driver = new ChromeDriver();
        driver.get("http://localhost:8080/usuarios");
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    void title(){
        String title = driver.getTitle();
        System.out.println(title);
        assertEquals("Lista de usuarios", title);
    }

    @Test
    void h1(){
        WebElement h1 = driver.findElement(By.tagName("h1"));
        assertEquals("Lista de usuarios", h1.getText());
    }

    @Test
    void botonCrearUsuario(){
        WebElement crearBoton = driver.findElement(By.id("btnCrearUsuario"));
        assertEquals("Crear nuevo usuario", crearBoton.getText());

        crearBoton.click();

        assertEquals("http://localhost:8080/usuarios/new", driver.getCurrentUrl());
    }

    @Test
    void tablaVacia(){
        WebElement mensajeNoProductos = driver.findElement(By.id("usuariosVacio"));
        assertEquals("No hay usuarios.", mensajeNoProductos.getText());

        assertThrows(
                NoSuchElementException.class,
                () -> driver.findElement(By.id("usuarioList"))
        );
    }

    @Test
    void tablaConUsuarios(){
        usuarioRepository.saveAll(List.of(
                Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(2L).nombreUsuario("Pedro").password("1331").nombre("Pedro").direccion("Calle 2").CP(12334).DNI("19797477M").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(3L).nombreUsuario("Carlos").password("1223").nombre("Carlos").direccion("Calle 3").CP(44147).DNI("13464497M").fechaCreacion(Date.from(Instant.now())).build()
        ));

        assertEquals(3, usuarioRepository.count());

        driver.navigate().refresh();

        WebElement usuarioList = driver.findElement(By.id("usuarioList"));
        assertTrue(usuarioList.isDisplayed());
    }


    @Test
    void tablaConUsuarios_Columnas(){
        usuarioRepository.saveAll(List.of(
                Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(2L).nombreUsuario("Pedro").password("1234").nombre("Pedro").direccion("Calle 2").CP(12334).DNI("19797477M").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(3L).nombreUsuario("Carlos").password("1234").nombre("Carlos").direccion("Calle 3").CP(44147).DNI("13464497M").fechaCreacion(Date.from(Instant.now())).build()
        ));

        driver.navigate().refresh();

        WebElement usuarioList = driver.findElement(By.id("usuarioList"));

        List<WebElement> headers = usuarioList.findElements(By.tagName("th"));
        assertEquals(9, headers.size());
        assertEquals("ID", headers.get(0).getText());
        assertEquals("NOMBRE DE USUARIO", headers.get(1).getText());
        assertEquals("PASSWORD", headers.get(2).getText());
        assertEquals("USUARIO", headers.get(3).getText());
        assertEquals("NOMBRE", headers.get(4).getText());
        assertEquals("DIRECCIÓN", headers.get(5).getText());
        assertEquals("CÓDIGO POSTAL", headers.get(6).getText());
        assertEquals("DNI", headers.get(7).getText());
        assertEquals("FECHA DE CREACIÓN", headers.get(8).getText());

    }

    @Test
    void tablaConUsuarios_Filas(){
        usuarioRepository.saveAll(List.of(
                Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(2L).nombreUsuario("Pedro").password("1234").nombre("Pedro").direccion("Calle 2").CP(12334).DNI("19797477M").fechaCreacion(Date.from(Instant.now())).build(),
                Usuario.builder().id(3L).nombreUsuario("Carlos").password("1234").nombre("Carlos").direccion("Calle 3").CP(44147).DNI("13464497M").fechaCreacion(Date.from(Instant.now())).build()
        ));

        assertEquals(3, usuarioRepository.count());

        driver.navigate().refresh();

        WebElement usuarioList = driver.findElement(By.id("usuarioList"));

        List<WebElement> columnas = usuarioList.findElements(By.tagName("tr"));
        assertEquals(4, columnas.size());

        List<WebElement> columnasTabla = usuarioList.findElements(By.cssSelector("#usuarioList tbody tr"));
        assertEquals(3, columnasTabla.size());

        WebElement firstRow = columnasTabla.getFirst();
        List<WebElement> firstRowDatas = firstRow.findElements(By.tagName("td"));

        assertEquals("Juan", firstRowDatas.get(1).getText());
    }

    @Test
    void tablaConUsuarios_Filas_Ids(){
        Usuario usuario = usuarioRepository.save(Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build());

        driver.navigate().refresh();

        WebElement id = driver.findElement(By.id("usuarioId_" + usuario.getId()));
        assertEquals(usuario.getId(), Long.valueOf(id.getText()));

        WebElement nombreUsuario = driver.findElement(By.id("usuarioNombreUsuario_" + usuario.getId()));
        assertEquals("Juan", nombreUsuario.getText());

        WebElement password = driver.findElement(By.id("usuarioPassword_" + usuario.getId()));
        assertEquals("1234", password.getText());

        WebElement nombre = driver.findElement(By.id("usuarioNombre_" + usuario.getId()));
        assertEquals("Juan Pérez", nombre.getText());

        WebElement direccion = driver.findElement(By.id("usuarioDireccion_" + usuario.getId()));
        assertEquals("Calle 1", direccion.getText());


    }

    @Test
    void tablaConUsuarios_accionBotonVer() {
        Usuario usuario = usuarioRepository.save(Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build());

        driver.navigate().refresh();

        var botonVer = driver.findElement(By.id("usuarioActionView_" + usuario.getId()));
        assertEquals("Ver", botonVer.getText());

        botonVer.click();

        assertEquals("http://localhost:8080/usuarios/" + usuario.getId(), driver.getCurrentUrl());
    }

    @Test
    void tablaConUsuarios_accionBotonEditar() {
        Usuario usuario = usuarioRepository.save(Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build());

        driver.navigate().refresh();

        var botonEditar = driver.findElement(By.id("usuarioActionEdit_" + usuario.getId()));
        assertEquals("Editar", botonEditar.getText());

        botonEditar.click();

        assertEquals("http://localhost:8080/usuarios/update/" + usuario.getId(), driver.getCurrentUrl());
    }
    @Test
    void tablaConUsuarios_accionBotonBorrar() {
        Usuario usuario = usuarioRepository.save(Usuario.builder().id(1L).nombreUsuario("Juan").password("1234").nombre("Juan Pérez").direccion("Calle 1").CP(15300).DNI("12345678O").fechaCreacion(Date.from(Instant.now())).build());

        driver.navigate().refresh();

        var botonBorrar = driver.findElement(By.id("usuarioActionDelete_" + usuario.getId()));
        assertEquals("Borrar", botonBorrar.getText());

        botonBorrar.click();

        assertEquals("http://localhost:8080/usuarios", driver.getCurrentUrl());
    }
}
