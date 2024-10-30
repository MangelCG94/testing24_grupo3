package com.games.games.controllers;

import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
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
public class UsuarioController {

    private UsuarioRepository usuarioRepository;

    //http://localhost:8080/usuarios
    @GetMapping("usuarios")
    public String encontrarTodos(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuario-list";
    }

    //http://localhost:8080/usuarios/1
    @GetMapping("usuarios/{id}")
    public String encontrarPorId(@PathVariable Long id, Model model) {
        usuarioRepository.findById(id).
                ifPresent(usuario -> model.addAttribute("usuario", usuario));
        return "usuario-detail";
    }

    @GetMapping("usuarios2/{id}")
    public String encontrarPorId2(@PathVariable Long id, Model model) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    return "usuario-detail";
                })
                .orElseGet(() -> {
                    model.addAttribute("mensaje", "Usuario no encontrado");
                    return "error";
                });
    }

    //http://localhost:8080/usuarios/new
    @GetMapping("usuarios/new")
    public String formularioParaCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario-form";
    }

    //http://localhost:8080/usuarios/edit/3
    @GetMapping("usuarios/update/{id}")
    public String formularioParaActualizar(@PathVariable Long id, Model model) {
        usuarioRepository.findById(id).
                ifPresent(usuario -> model.addAttribute("usuario", usuario));
        return "usuario-form";
    }

    @PostMapping("usuarios")
    public String guardar(@ModelAttribute Usuario usuario) {
        if (usuario.getId() == null) {
            usuarioRepository.save(usuario);
        } else {
            usuarioRepository.findById(usuario.getId()).ifPresent(usuarioDB -> {
                BeanUtils.copyProperties(usuario, usuarioDB);
                usuarioRepository.save(usuarioDB);
            });
        }
        return "redirect:/usuarios";
    }

    @GetMapping("usuarios/delete/{id}")
    public String borrarPorId(@PathVariable Long id) {
        try {
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/usuarios";
    }

    @GetMapping("usuarios/deleteAll")
    public String borrarTodo() {
        try {
            usuarioRepository.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/usuarios";
    }
}
