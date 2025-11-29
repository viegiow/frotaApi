package com.example.frota.percurso;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PercursoRepository extends JpaRepository<Percurso, Long> {

    Optional<Percurso> findByCaminhaoIdAndStatus(Long caminhaoId, StatusPercurso status);

    List<Percurso> findAllByCaminhaoId(Long caminhaoId);
}