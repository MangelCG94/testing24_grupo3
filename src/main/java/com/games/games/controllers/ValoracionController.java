package com.games.games.controllers;

import com.games.games.models.Valoracion;
import com.games.games.repositories.ValoracionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class ValoracionController {
    private ValoracionRepository valoracionRepository;
    @GetMapping("valoraciones")
    public String findAll(Model model){
        model.addAttribute("valoraciones",
                valoracionRepository.findAll());
        return "valoracion-list";
    }
    @GetMapping("valoraciones/{id}")
    public String findById(@PathVariable Long id, Model model){
        valoracionRepository.findById(id)
                .ifPresent(valoracion -> model.addAttribute("valoracion",valoracion));
        return "valoracion-detail";
    }
    @GetMapping("valoracion/new")
    public String getFormToCreate(Model model){
        Valoracion valoracion = new Valoracion();
        model.addAttribute("valoracion", valoracion);
        return "valoracion-form";
    }
    @GetMapping("valoracion/update/{id}")
    public String formularioParaActualizar(@PathVariable Long id,Model model){
        valoracionRepository.findById(id)
                .ifPresent(valoracion -> model.addAttribute("valoracion", valoracion));
        return "valoracion-form";
    }
    @PostMapping("valoracion")
    public String save(@ModelAttribute Valoracion valoracion){
        if (valoracion.getId() == null || !valoracionRepository.existsById(valoracion.getId())){
            valoracionRepository.save(valoracion);
        } else {
            valoracionRepository.findById(valoracion.getId())
                    .ifPresent(oldValoracion->{
                        BeanUtils.copyProperties(valoracion,oldValoracion);
                        valoracionRepository.save(oldValoracion);
                    });
        }
        return "redirect:/valoracion";
    }
}
