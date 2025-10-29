package com.example.frota.parametros;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ParametroRepository extends JpaRepository<Parametro, Long>{
    Optional<Parametro> findByNome(String nome);

}