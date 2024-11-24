package com.games.games.controllers;
import com.games.games.models.Juego;
import com.games.games.models.Usuario;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.games.games.models.Valoracion;
import com.games.games.repositories.ValoracionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
public class ValoracionController {
    private ValoracionRepository valoracionRepository;

    @GetMapping ("valoraciones")
    public String findAll(Model model) {
        model.addAttribute("valoraciones",
                valoracionRepository.findAll());
        return "valoracion-list";
    }

    @GetMapping ("valoraciones/{id}")
    public String findById(@PathVariable Long id, Model model) {
        valoracionRepository.findById(id)
                .ifPresent(valoracion -> model.addAttribute("valoracion", valoracion));
        return "valoracion-detail";
    }

    @GetMapping ("valoracion/new")
    public String getFormToCreate(Model model) {
        Valoracion valoracion = new Valoracion();
        model.addAttribute("valoracion", valoracion);
        return "valoracion-form";
    }

    @GetMapping ("valoracion/update/{id}")
    public String formularioParaActualizar(@PathVariable Long id, Model model) {
        valoracionRepository.findById(id)
                .ifPresent(valoracion -> model.addAttribute("valoracion", valoracion));
        return "valoracion-form";
    }

    @PostMapping ("valoracion")
    public String save(@ModelAttribute Valoracion valoracion) {
        if (valoracion.getId() == null || !valoracionRepository.existsById(valoracion.getId())) {
            valoracionRepository.save(valoracion);
        } else {
            valoracionRepository.findById(valoracion.getId())
                    .ifPresent(oldValoracion -> {
                        BeanUtils.copyProperties(valoracion, oldValoracion);
                        valoracionRepository.save(oldValoracion);
                    });
        }
        return "redirect:/valoracion";
    }
    @GetMapping ("valoracion/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        try {
            valoracionRepository.deleteById(id);
        } catch (Exception e) {
            return logAndReturnError(e);
        }
        return "redirect:/valoraciones";
    }
    private String logAndReturnError(Exception e) {
        e.printStackTrace();
        return "error";
    }

   //@RestController
   //@RequestMapping("/valoraciones")
    //private static class ValoracionController1{

       // private final ValoracionRepository valoracionRepository;

       // @Autowired
        //public ValoracionController1(ValoracionRepository valoracionRepository) {
         //   this.valoracionRepository = valoracionRepository;
        }

       // @GetMapping("/{id}")
       // public ResponseEntity<Valoracion> obtenerValoracion(@PathVariable Long id) {
       //     return valoracionRepository.findById(id)
        //            .map(ResponseEntity::ok)
        //            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
       // }

       // @PostMapping
       // public ResponseEntity<Valoracion> guardarValoracion(@RequestBody Valoracion valoracion) {
       //     Valoracion valoracionGuardada = valoracionRepository.save(valoracion);
       //     return ResponseEntity.status(HttpStatus.CREATED).body(valoracionGuardada);
       // }
   // }
       // }






