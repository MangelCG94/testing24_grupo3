package com.games.games.controllers;

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

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RestController
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

    // Métodos con API Rest

    @GetMapping("usuarios") // localhost:8080/usuarios?nombre=Juan
    public ResponseEntity<String> encuentraNombreUsuario(@RequestParam(required = false) String nombreUsuario) {
        return ResponseEntity.ok("Bienvenido, " + nombreUsuario);
    }

    // CRUD

    @GetMapping("usuarios") // localhost:8080/usuarios
    public ResponseEntity<List<Usuario>> encuentraTodosUsuariosAPI() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("usuarios/{id}") // localhost:8080/usuarios/1
    public ResponseEntity<Usuario> encontrarUsuarioPorID_API (@PathVariable Long id){
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("usuarios")
    public ResponseEntity<Usuario> crearUsuarioAPI(@RequestBody Usuario usuario){
        if (usuario.getId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PostMapping("usuarios/filtro")
    public ResponseEntity<List<Usuario>> encuentraPorFiltro(@RequestBody Usuario usuario){
        var usuarios = usuarioRepository.findAll(Example.of(usuario));

        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("usuarios")
    public ResponseEntity<Usuario> editarUsuarioAPI(@RequestBody Usuario usuario){
        if (usuario.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("usuarios/{id}")
    public ResponseEntity<Void> borrarUsuarioAPI(@PathVariable Long id){
        try {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    // Método PATCH
    // Actualización parcial de un usuario
    @PatchMapping(value = "usuarios/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<Usuario> actualizacionParcial(
            @PathVariable Long id, @RequestBody Usuario usuario
    ) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    if (usuario.getNombreUsuario() != null) usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
                    if (usuario.getPassword() != null) usuarioExistente.setPassword(usuario.getPassword());
                    if (usuario.getNombre() != null) usuarioExistente.setNombre(usuario.getNombre());
                    if (usuario.getDireccion() != null) usuarioExistente.setDireccion(usuario.getDireccion());
                    if (usuario.getCP() != null) usuarioExistente.setCP(usuario.getCP());
                    if (usuario.getDNI() != null) usuarioExistente.setDNI(usuario.getDNI());
                    if (usuario.getFechaCreacion() != null) usuarioExistente.setFechaCreacion(usuario.getFechaCreacion());
                    usuarioRepository.save(usuarioExistente);
                    return ResponseEntity.ok(usuarioExistente);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("usuarios")
    public ResponseEntity<Void> borrarTodo(@RequestBody List<Long> ids){
        try {
            usuarioRepository.deleteAllByIdInBatch(ids);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar un cliente", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error al eliminar un cliente");

        }
    }
}
