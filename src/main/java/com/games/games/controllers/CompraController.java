package com.games.games.controllers;

import com.games.games.models.Compra;
import com.games.games.models.JuegosUsuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.JuegosUsuarioRepository;
import com.games.games.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
public class CompraController {

    private CompraRepository compraRepository;
    private UsuarioRepository usuarioRepository;
    private JuegoRepository juegoRepository;

    //http://localhost:8080/compras
    @GetMapping("compras")
    public String encontrarTodasCompras(Model model) {
        model.addAttribute("compras", compraRepository.findAll());
        return "compra-list";
    }

    //http://localhost:8080/compras/1
    @GetMapping("compras/{id}")
    public String encontrarPorIdCompra(@PathVariable Long id, Model model) {
        compraRepository.findById(id).
                ifPresent(compra -> model.addAttribute("compra", compra));
        return "compra-detail";
    }

    @GetMapping("compras2/{id}")
    public String encontrarPorIdCompra2(@PathVariable Long id, Model model) {
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
        model.addAttribute("juego", juegoRepository.findAll());
        model.addAttribute("usuario", usuarioRepository.findAll());
        return "compra-form";
    }

    //http://localhost:8080/compras/edit/1
    @GetMapping("compras/update/{id}")
    public String formularioParaActualizarCompra(@PathVariable Long id, Model model) {
        compraRepository.findById(id).
                ifPresent(compra -> model.addAttribute("compra", compra));
        model.addAttribute("juego", juegoRepository.findAll());
        model.addAttribute("usuario", usuarioRepository.findAll());
        return "compra-form";
    }

    @PostMapping("compras")
    public String guardarCompra(@ModelAttribute Compra compra) {
        if (compra.getIdCompra() == null) {
            compraRepository.save(compra);
        } else {
            compraRepository.findById(compra.getIdCompra()).ifPresent(compraDB -> {
                compraDB.setFechaCompra(compra.getFechaCompra());
                compraDB.setUsuario(compra.getUsuario());
                compraDB.setJuego(compra.getJuego());
                compraRepository.save(compraDB);
            });
        }
        return "redirect:/compras";

    }

    //http://localhost:8080/compras/delete/1
    @GetMapping("compras/delete/{id}")
    public String borrarPorIdCompra(@PathVariable Long id) {
        try {
            compraRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/compras";


    }

    @GetMapping("compras/delete/all")
    public String borrarTodasLasCompras() {
        try {
            compraRepository.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/compras";
    }

}
