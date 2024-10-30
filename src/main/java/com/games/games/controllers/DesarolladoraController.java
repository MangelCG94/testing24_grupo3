package com.games.games.controllers;

import com.games.games.models.Desarrolladora;
import com.games.games.repositories.DesarrolladoraRepository;
import lombok.*;
import org.springframework.*;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class DesarolladoraController {
    private DesarrolladoraRepository desarrolladoraRepo;

    @GetMapping("Desarrolladoras")
    public String findAll(Model model){

    }
}
