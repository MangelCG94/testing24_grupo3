package com.games.games.controllers;

import com.games.games.models.Desarrolladora;
import com.games.games.repositories.DesarrolladoraRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Controller
public class DesarrolladoraController {
    private DesarrolladoraRepository desarrolladoraRepo;

    @GetMapping("desarrolladoras")
    public String findAll(Model model){
        model.addAttribute("desarrolladoras",
                desarrolladoraRepo.findAll());
        return "desarrolladora-list";
    }

    @GetMapping("desarrolladoras/{id}")
    public String findById(@PathVariable Long id, Model model) {
        return desarrolladoraRepo.findById(id)
                .map(desarrolladora -> {
                    model.addAttribute("desarrolladora", desarrolladora);
                    return "desarrolladora-detail";
                }).orElseThrow(() ->
                        new NoSuchElementException("Desarrolladora no encontrada"));
    }


    @GetMapping("desarrolladoras/new")
    public String getFormToCreate(Model model) {
        Desarrolladora desarrolladora = new Desarrolladora();
        model.addAttribute("desarrolladora", desarrolladora);
        return "desarrolladora-form";
    }

    @GetMapping("desarrolladoras/update/{id}")
    public String formularioParaActualizar(@PathVariable Long id, Model model) {
        desarrolladoraRepo.findById(id).ifPresent(desarrolladora -> model.addAttribute("desarrolladora", desarrolladora));
        return "desarrolladora-form";
    }
    @PostMapping("desarrolladoras")
    public String save(@ModelAttribute Desarrolladora desarrolladora) {
        if (desarrolladora.getId() == null || !desarrolladoraRepo.existsById(desarrolladora.getId())) {
            desarrolladoraRepo.save(desarrolladora);
        } else {// else updates
            desarrolladoraRepo.findById(desarrolladora.getId())
                    .ifPresent(oldJuego -> {
                        BeanUtils.copyProperties(desarrolladora,oldJuego);
                        desarrolladoraRepo.save(oldJuego);
                    });
        }

        return "redirect:/desarrolladoras";
    }

    @GetMapping("desarrolladoras/delete/{id}")
    public String borrarPorId(@PathVariable Long id) {
        try {
            desarrolladoraRepo.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/desarrolladoras";
    }
}
