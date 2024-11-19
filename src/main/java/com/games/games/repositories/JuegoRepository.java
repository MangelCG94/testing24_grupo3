package com.games.games.repositories;

import com.games.games.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JuegoRepository extends JpaRepository<Juego, Long> {
    List<Juego> findByDesarrolladoraNombreCom(String nombreCom);

    List<Juego> findByPrecioLessThan(Double precio); //LessThan = MenosQue

    @Query("SELECT j FROM Juego j WHERE j.desarrolladora.pais = :pais")
    List<Juego> findByDesarrolladoraPais(@Param("pais") String pais);

    @Query("SELECT j FROM Juego j " +
            "WHERE j.precio < :maxPrice " +
            "AND j.desarrolladora.anyoFundacion > :year")
    List<Juego> findByPriceAndFoundationYear(@Param("maxPrice") Double maxPrice, @Param("year") Integer year);
}