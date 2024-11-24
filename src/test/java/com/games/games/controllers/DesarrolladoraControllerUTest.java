package com.games.games.controllers;

import com.games.games.dtos.DetalleUsuario;
import com.games.games.models.Desarrolladora;
import com.games.games.models.Usuario;
import com.games.games.repositories.DesarrolladoraRepository;
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
    private DesarrolladoraController desarolladoraController;
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

        String view = desarolladoraController.findAll(model);

        assertEquals("desarrolladora-list", view);
        verify(desarrolladoraRepository).findAll();
    }

    @Test
    void encontrarPorIdCuandoNoExisteDesarrolladora(){
        when(desarrolladoraRepository.findById(1L)).thenReturn(Optional.empty());

        String view = desarolladoraController.findById(1L, model);

    }
}