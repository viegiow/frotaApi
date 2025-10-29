package com.example.frota.caixa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CaixaRepository extends JpaRepository<Caixa, Long> {
	@Query("SELECT c FROM Caixa c WHERE c.comprimento >= :comp AND c.altura >= :alt AND c.largura >= :larg AND c.limitePeso >= :peso")
	List<Caixa> findCompativeis(@Param("comp") int comprimento, 
	                             @Param("alt") int altura, 
	                             @Param("larg") int largura,
	                             @Param("peso") Double pesoMax);


}
