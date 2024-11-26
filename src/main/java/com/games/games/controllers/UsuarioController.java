package com.games.games.controllers;

import com.games.games.dtos.DetalleUsuario;
import com.games.games.models.Usuario;
import com.games.games.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
@Controller
public class UsuarioController {

    private UsuarioRepository usuarioRepository;

    //http://localhost:8080/usuarios
    @GetMapping("usuarios")
    public String encontrarTodos(Model model) {
        model.addAttribute("titulo", "Lista de usuarios");
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuario-list";
    }

    //http://localhost:8080/usuarios/1
    @GetMapping("usuarios/{id}")
    public String encontrarPorId(@PathVariable Long id, Model model) {
        usuarioRepository.findById(id).
                ifPresent(usuario -> model.addAttribute("usuario", new DetalleUsuario(usuario)));
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
//                .orElseThrow(() ->
//                        new NoSuchElementException("Usuario no encontrado"));
    }

    //http://localhost:8080/usuarios/new
    @GetMapping("usuarios/new")
    public String formularioParaCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario-form";
    }

    //http://localhost:8080/usuarios/update/3
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

    @GetMapping("usuarios/delete/all")
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
