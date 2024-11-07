package com.games.games.controllers;

import com.games.games.models.Desarrolladora;
import com.games.games.models.Juego;
import com.games.games.repositories.DesarrolladoraRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Controller
public class DesarolladoraController {
    private DesarrolladoraRepository desarrolladoraRepo;

    @GetMapping("Desarrolladoras")
    public String findAll(Model model){
        model.addAttribute("desarolladoras",
                desarrolladoraRepo.findAll());
        return "desarolladora-list";
    }

}
