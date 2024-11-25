package com.games.games.controllers;

import com.games.games.dtos.DetalleUsuario;
import com.games.games.models.Desarrolladora;
import com.games.games.models.Usuario;
import com.games.games.repositories.DesarrolladoraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesarrolladoraControllerUTest {

    @InjectMocks
    private DesarrolladoraController desarrolladoraController;
    @Mock
    private DesarrolladoraRepository desarrolladoraRepository;
    @Mock
    private Model model;

    @Test
    void encontrarTodos(){
        when(desarrolladoraRepository.findAll()).thenReturn(List.of(
                Desarrolladora.builder().id(1L).nombreCom("Nintendo").pais("Japón").imagenLogo("logo.png").anyoFundacion(1900).build(),
                Desarrolladora.builder().id(2L).nombreCom("Ubisoft").pais("Francia").imagenLogo("logo.png").anyoFundacion(1988).build()
        ));

        String view = desarrolladoraController.findAll(model);

        assertEquals("desarrolladora-list", view);
        verify(desarrolladoraRepository).findAll();
    }

    @Test
    void encontrarPorIdCuandoExisteDesarrolladora(){
        Desarrolladora nintendo = Desarrolladora.builder().id(1L).nombreCom("Nitendo").pais("Japón").imagenLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Nintendo.svg/900px-Nintendo.svg.png").anyoFundacion(1900).build();
        when(desarrolladoraRepository.findById(1L)).thenReturn(Optional.of(nintendo));

        String view = desarrolladoraController.findById((1L),model);

        assertEquals("desarrolladora-detail", view);
        verify(desarrolladoraRepository).findById(1L);
        verify(desarrolladoraRepository, never()).findAll();
    }

    @Test
    void encontrarPorIdCuandoNoExisteDesarrolladora(){
        when(desarrolladoraRepository.findById(1L)).thenReturn(Optional.empty());

        String view = desarrolladoraController.findById(1L, model);

        assertEquals("desarrolladora-detail", view);
        verify(desarrolladoraRepository).findById(1L);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void formularioParaCrear(){
        String view = desarrolladoraController.getFormToCreate(model);

        assertEquals("desarrolladora-form", view);
        verify(model).addAttribute(eq("desarrolladora"), any(Desarrolladora.class));
    }

    @Test
    void formularioParaActualizarSiExisteDesarrolladora(){
        Desarrolladora nintendo = Desarrolladora.builder().id(1L).nombreCom("Nintendo").pais("Japón").imagenLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Nintendo.svg/900px-Nintendo.svg.png").anyoFundacion(1900).build();
        when(desarrolladoraRepository.findById(1L)).thenReturn(Optional.of(nintendo));

        String view = desarrolladoraController.formularioParaActualizar((1L), model);

        assertEquals("desarrolladora-form", view);
        verify(desarrolladoraRepository).findById(1L);
        verify(model).addAttribute("desarrolladora", nintendo);
    }

    @Test
    void formularioParaActualizarSiNoExisteDesarrolladora(){
        when(desarrolladoraRepository.findById(1L)).thenReturn(Optional.empty());

        String view = desarrolladoraController.formularioParaActualizar(1L, model);

        assertEquals("desarrolladora-form", view);
        verify(desarrolladoraRepository).findById(1L);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void guardarDesarrolladoraNueva(){
        Desarrolladora desarrolladora = new Desarrolladora();

        String view = desarrolladoraController.save(desarrolladora);

        assertEquals("redirect:/desarrolladoras", view);
        verify(desarrolladoraRepository).save(desarrolladora);
    }

    @Test
    void guardarDesarrolladoraSiExiste(){
        Desarrolladora desarrolladora = Desarrolladora.builder().id(1L).nombreCom("Ubisoft").pais("Francia").imagenLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Ubisoft_logo.svg/983px-Ubisoft_logo.svg.png").anyoFundacion(1988).build();
        Desarrolladora desarrolladoraActualizada = Desarrolladora.builder().id(1L).nombreCom("Nintendo").pais("Japón").imagenLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Nintendo.svg/900px-Nintendo.svg.png").anyoFundacion(1900).build();

        when(desarrolladoraRepository.findById(1L)).thenReturn(Optional.of(desarrolladora));

        String view = desarrolladoraController.save(desarrolladoraActualizada);

        assertEquals("redirect:/desarrolladoras", view);
        verify(desarrolladoraRepository).findById(1L);
        verify(desarrolladoraRepository).save(desarrolladora);
        assertEquals(desarrolladoraActualizada.getNombreCom(), desarrolladora.getNombreCom());
        assertEquals(desarrolladoraActualizada.getPais(), desarrolladora.getPais());
        assertEquals(desarrolladoraActualizada.getImagenLogo(), desarrolladora.getImagenLogo());
        assertEquals(desarrolladoraActualizada.getAnyoFundacion(), desarrolladora.getAnyoFundacion());
    }

    @Test
    void borrarPorId(){
        String view = desarrolladoraController.borrarPorId(1L);

        assertEquals("redirect:/desarrolladoras", view);
        verify(desarrolladoraRepository).deleteById(1L);
    }

    @Test
    void borrarPorId_ErrorCapturado(){
        doThrow(new RuntimeException("Error al borrar")).when(desarrolladoraRepository).deleteById(1L);

        String view = desarrolladoraController.borrarPorId(1L);

        assertEquals("error", view);
        verify(desarrolladoraRepository).deleteById(1L);
    }
}