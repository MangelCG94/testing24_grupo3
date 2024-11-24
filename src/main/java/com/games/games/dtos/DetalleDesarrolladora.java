package com.games.games.dtos;

import com.games.games.models.Desarrolladora;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
@EqualsAndHashCode
public class DetalleDesarrolladora {
    private Long id;
    private String nombreCom;
    private String pais;
    private String imagenLogo;
    private Integer anyoFundacion;

    public DetalleDesarrolladora(Desarrolladora desarrolladora){
        this.id = desarrolladora.getId();
        this.nombreCom = desarrolladora.getNombreCom();
        this.pais = desarrolladora.getPais();
        this.imagenLogo = desarrolladora.getImagenLogo();
        this.anyoFundacion = desarrolladora.getAnyoFundacion();
    }
}
