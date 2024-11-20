package com.games.games.controllers;

import com.games.games.models.Valoracion;
import com.games.games.repositories.ValoracionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
public class ValoracionControllerUTest {
@InjectMocks
    private ValoracionController valoracionController;

@Mock
    private ValoracionRepository valoracionRepository;
@Mock
    private Model model;

    @Test
    void findAll(){
        when(valoracionRepository.findAll()).thenReturn(List.of(
                Valoracion.builder().id(1L).build(),
                Valoracion.builder().id(2L).build(),
                Valoracion.builder().id(3L).build()
        ));
        String view = valoracionController.findAll(model);
        assertEquals("valoracion-list", view);
        verify(valoracionRepository).findAll();
    }
}
