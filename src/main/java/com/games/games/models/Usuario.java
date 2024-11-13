package com.games.games.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String nombreUsuario;
    private String password;
    private String nombre;
    private String direccion;
    private Integer CP;
    private String DNI;
    private Date fechaCreacion;
}
