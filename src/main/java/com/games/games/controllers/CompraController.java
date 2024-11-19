package com.games.games.controllers;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.JuegosUsuario;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.JuegosUsuarioRepository;
import com.games.games.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Controller
public class CompraController {

    private CompraRepository compraRepository;
    private UsuarioRepository usuarioRepository;
    private JuegoRepository juegoRepository;

    //http://localhost:8080/compras
    @GetMapping("compras")
    public String encontrarTodasCompras(Model model) {
        model.addAttribute("titulo", "Lista de compras");
        model.addAttribute("compras", compraRepository.findAll());
        return "compra-list";
    }

    //http://localhost:8080/compras/1
    @GetMapping("compras/{id}")
    public String encontrarPorId(@PathVariable Long id, Model model) {
        compraRepository.findById(id).
                ifPresent(compra -> model.addAttribute("compra", compra));
        return "compra-detail";
    }

    @GetMapping("compras2/{id}")
    public String encontrarPorId2(@PathVariable Long id, Model model) {
        return compraRepository.findById(id)
                .map(compra -> {
                    model.addAttribute("compra", compra);
                    return "compra-detail";
                })
                .orElseGet(() -> {
                    model.addAttribute("mensaje", "Compra no encontrada");
                    return "error";
                });
    }

    //http://localhost:8080/compras/new
    @GetMapping("compras/new")
    public String formularioParaCrearCompra(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("juego", new Juego());
        model.addAttribute("usuario", new Usuario());
        return "compra-form";
    }

    //http://localhost:8080/compras/update/1
    @GetMapping("compras/update/{id}")
    public String formularioParaActualizarCompra(@PathVariable Long id, Model model) {
        compraRepository.findById(id).
                ifPresent(compra -> model.addAttribute("compra", compra));
        return "compra-form";
    }

    @PostMapping("compras")
    public String guardarCompra(@ModelAttribute Compra compra) {
        if (compra.getId() == null) {
            compraRepository.save(compra);
        } else {
            compraRepository.findById(compra.getId()).ifPresent(compraDB -> {
                compraDB.setFechaCompra(compra.getFechaCompra());
                compraDB.setUsuario(compra.getUsuario());
                compraDB.setJuego(compra.getJuego());
                compraRepository.save(compraDB);
            });
        }
        return "redirect:/compras";

    }

    //http://localhost:8080/compras/borrar/1
    @GetMapping("compras/delete/{id}")
    public String borrarPorId(@PathVariable Long id) {
        try {
            compraRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/compras";


    }


}
