package com.games.games.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class Desarrolladora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCom;
    private String pais;
    // imagenLogo se refiere al archivo PNG
    private String imagenLogo;
    private Integer anyoFundacion;
}
