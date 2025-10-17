package com.example.frota.caixa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CaixaRepository extends JpaRepository<Caixa, Long> {

}
