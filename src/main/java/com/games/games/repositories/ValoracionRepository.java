package com.games.games.repositories;

import com.games.games.models.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValoracionRepository extends JpaRepository<Valoracion, Integer> {
  }