package com.games.games.dtos;

import com.games.games.models.Usuario;
import lombok.Getter;
import lombok.ToString;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class DetalleUsuario {
    private Long id;
    private String nombreUsuario;
    private String password;
    private String nombre;
    private String direccion;
    private Integer CP;
    private String DNI;
    private String fechaCreacion;

    public DetalleUsuario(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.password = usuario.getPassword();
        this.nombre = usuario.getNombre();
        this.direccion = usuario.getDireccion();
        this.CP = usuario.getCP();
        this.DNI = usuario.getDNI();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        this.fechaCreacion = formatter.format(usuario.getFechaCreacion());
    }

}
