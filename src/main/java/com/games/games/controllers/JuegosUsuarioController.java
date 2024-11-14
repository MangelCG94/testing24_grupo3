package com.games.games.controllers;

import com.games.games.models.JuegosUsuario;
import com.games.games.repositories.JuegosUsuarioRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.UsuarioRepository;
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
public class JuegosUsuarioController {

    private final JuegosUsuarioRepository juegosUsuarioRepository;
    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("juegosUsuario")
    public String findAll(Model model) {
        model.addAttribute("title", "Lista de Juegos de Usuario");
        model.addAttribute("juegosUsuarios", juegosUsuarioRepository.findAll());
        return "juegosUsuario-list";
    }

    @GetMapping("juegosUsuario/{id}")
    public String findById(@PathVariable Long id, Model model) {
        return juegosUsuarioRepository.findById(id)
                .map(juegosUsuario -> {
                    model.addAttribute("juegosUsuario", juegosUsuario);
                    return "juegosUsuario-detail";
                }).orElseThrow(() ->
                        new NoSuchElementException("No hay juegos de usuario"));
    }

    @GetMapping("juegosUsuario/new")
    public String formCreate(Model model) {
        model.addAttribute("juegosUsuario", new JuegosUsuario());
        model.addAttribute("juegos", juegoRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "juegosUsuario-form";
    }

    @GetMapping("juegosUsuario/editar/{id}")
    public String formUpdate(@PathVariable Long id, Model model) {
        juegosUsuarioRepository.findById(id).ifPresent(juegosUsuario -> {
            model.addAttribute("juegosUsuario", juegosUsuario);
            model.addAttribute("juegos", juegoRepository.findAll());
            model.addAttribute("usuarios", usuarioRepository.findAll());
        });
        return "juegosUsuario-form";
    }

    @PostMapping("juegosUsuario")
    public String save(@ModelAttribute JuegosUsuario juegosUsuario) {
        if (juegosUsuario.getId() == null || !juegosUsuarioRepository.existsById(juegosUsuario.getId())) {
            juegosUsuarioRepository.save(juegosUsuario);
        } else { // Update existing entry
            juegosUsuarioRepository.findById(juegosUsuario.getId())
                    .ifPresent(existing -> {
                        BeanUtils.copyProperties(juegosUsuario, existing, "id");
                        juegosUsuarioRepository.save(existing);
                    });
        }
        return "redirect:/juegosUsuario";
    }

    @GetMapping("juegosUsuario/borrar/{id}")
    public String delete(@PathVariable Long id) {
        try {
            juegosUsuarioRepository.deleteById(id);
            return "redirect:/juegosUsuario";
        } catch (Exception e) {
            return "error";
        }
    }
}
