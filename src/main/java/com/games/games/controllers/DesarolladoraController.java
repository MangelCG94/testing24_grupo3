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

    @GetMapping("desarrolladoras")
    public String findAll(Model model){
        model.addAttribute("desarrolladoras",
                desarrolladoraRepo.findAll());
        return "desarrolladora-list";
    }

    @GetMapping("desarrolladoras/{id}")
    public String findById(@PathVariable Long id, Model model) {
        desarrolladoraRepo.findById(id)
                .ifPresent(manufacturer -> model.addAttribute("manufacturer", manufacturer));
        return "desarrolladora-detail";
    }


    @GetMapping("desarrolladoras/new")
    public String getFormToCreate(Model model) {
        Desarrolladora desarrolladora = new Desarrolladora();
        model.addAttribute("desarrolladora", desarrolladora);
        return "desarrolladora-form";
    }

    @GetMapping("desarrolladoras/update/{id}")
    public String formularioParaActualizar(@PathVariable Long id, Model model) {
        desarrolladoraRepo.findById(id).
                ifPresent(desarrolladora -> model.addAttribute("desarrolladora", desarrolladora));
        return "desarrolladora-form";
    }
}
