package com.games.games.controllers;

import com.games.games.models.Juego;
import com.games.games.repositories.JuegoRepository;
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
public class JuegoController {
    private final JuegoRepository repository;

    @GetMapping("juegos")
    public String findAll(Model model) {
        model.addAttribute("title","Lista de Juegos");
        model.addAttribute("juegos", repository.findAll());
        return "juego-list";
    }

    @GetMapping("customers/{id}")
    public String findById(@PathVariable Long id, Model model) {
        return repository.findById(id)
                .map(customer -> {
                    model.addAttribute("customer", customer);
                    return "customer-detail";
                }).orElseThrow(() ->
                        new NoSuchElementException("Customer not found"));
    }

    @PostMapping("juegos")
    public String saveCustomer(@ModelAttribute Juego juego) {
        if (juego.getId() == null || !repository.existsById(juego.getId())) {
            repository.save(juego);
        } else {// else updates
            repository.findById(juego.getId())
                    .ifPresent(oldJuego -> {
                        BeanUtils.copyProperties(juego,oldJuego);
                        repository.save(oldJuego);
                    });
        }

        return "redirect:/juegos";
    }

    @GetMapping("juegos/borrar/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return "redirect:/juegos";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }





    @GetMapping("juegos/new")
    public String formCreate(Model model) {
        model.addAttribute("juego",new Juego());

        return "juego-form";
    }

    @GetMapping("juegos/editar/{id}")
    public String formUpdate(Model model, @PathVariable Long id) {
        repository.findById(id).ifPresent(juego -> {
            model.addAttribute("juego", juego);
        });

        return "juego-form";
    }
}
