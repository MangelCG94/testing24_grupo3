package com.games.games.controllers;

import com.games.games.models.Juego;
import com.games.games.repositories.JuegoRepository;
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

@ExtendWith(MockitoExtension.class)
public class JuegoControllerUTest {
    @InjectMocks
    private JuegoController controller;
    @Mock
    private JuegoRepository repository;
    @Mock
    private Model model;

    @Test
    void findAllTest() {
        when(repository.findAll()).thenReturn(List.of(
                Juego.builder().build(),
                Juego.builder().build()));

        String view = controller.findAll(model);

        assertEquals("juego-list", view);
        verify(repository).findAll();
    }


}
